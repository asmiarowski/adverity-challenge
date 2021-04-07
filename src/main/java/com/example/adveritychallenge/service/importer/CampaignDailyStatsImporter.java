package com.example.adveritychallenge.service.importer;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.repository.CampaignDailyStatsRepository;
import com.example.adveritychallenge.service.importer.lock.ImportLock;
import com.example.adveritychallenge.service.importer.lock.ImportLockRepository;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.adveritychallenge.service.importer.lock.ImporterType.CAMPAIGN_DAILY_STATS_CSV;

@Service
@Slf4j
@RequiredArgsConstructor
public class CampaignDailyStatsImporter {

    private final ImportLockRepository importLockRepository;

    private final CampaignDailyStatsRepository campaignDailyStatsRepository;

    private final ImporterProperties importerProperties;

    /**
     * How many lines were properly processed.
     */
    private int successes = 0;

    /**
     * How many lines were unprocessed.
     */
    private int failures = 0;

    /**
     * List of read and prepared entities for save. This list is filled until SAVE_BULK_ON_COUNT is reached, then
     * its saved and cleared.
     */
    private final List<CampaignDailyStats> campaignDailyStatsList = new ArrayList<>();

    /**
     * After campaignDailyStatsList reach this count, we dump the list to database as bulk inserts and clear it for
     * further processing.
     */
    private static final int SAVE_BULK_ON_COUNT = 1000;

    public void run() {
        log.info("Starting campaign daily stats import from csv file.");

        var lock = importLockRepository.findByImporterType(CAMPAIGN_DAILY_STATS_CSV)
                .orElse(new ImportLock(CAMPAIGN_DAILY_STATS_CSV));

        if (lock.isLocked()) {
            log.info("Campaign daily stats already imported. Skipping.");
        }

        var file = new File(importerProperties.getDailyCampaignFilePath());

        try (Reader reader = new FileReader(file)) {
            var objectReader = getObjectReader();
            var mappingIterator = objectReader.readValues(reader);
            while (mappingIterator.hasNext()) {
                readLine(mappingIterator);

                if (campaignDailyStatsList.size() >= SAVE_BULK_ON_COUNT) {
                    saveBulk();
                }
            }

            if (!campaignDailyStatsList.isEmpty()) {
                saveBulk();
            }
        } catch (FileNotFoundException e) {
            log.error("Unable to find file {}", file);
            return;
        } catch (IOException e) {
            log.error("An error occurred while reading file {}", file, e);
            return;
        }

        log.info("Finished importing campaign daily stats from CSV. Imported {} records, and {} records failed",
                successes, failures);

        cleanup();

        lock.setLocked(true);
        importLockRepository.save(lock);
    }

    private ObjectReader getObjectReader() {
        var csvMapper = new CsvMapper();
        var schema = CsvSchema.emptySchema().withHeader();

        return csvMapper.readerFor(CampaignDailyStatsCsvLine.class).with(schema);
    }

    private void readLine(com.fasterxml.jackson.databind.MappingIterator<Object> mappingIterator) {
        try {
            var line = (CampaignDailyStatsCsvLine) mappingIterator.next();

            campaignDailyStatsList.add(mapCsvLineToEntity(line));
        } catch (Exception e) {
            failures++;
            log.warn("Unable to read line. Skipping", e);
        }
    }

    private CampaignDailyStats mapCsvLineToEntity(CampaignDailyStatsCsvLine line) {
        var campaignDailyStats = new CampaignDailyStats();
        BeanUtils.copyProperties(line, campaignDailyStats);

        var datetimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        campaignDailyStats.setDay(LocalDate.parse(line.getDaily(), datetimeFormatter));

        return campaignDailyStats;
    }

    /**
     * We save entities in bulk to prevent OOM error from having too many objects in memory as the file has hundreds
     * of thousands of records.
     */
    private void saveBulk() {
        campaignDailyStatsRepository.saveAll(campaignDailyStatsList);

        successes += campaignDailyStatsList.size();
        log.debug("Saved {} entries in total", successes);

        campaignDailyStatsList.clear();
    }

    /**
     * In case the script will be repeated by any means, we want the state to be clean again.
     */
    private void cleanup() {
        successes = 0;
        failures = 0;
    }
}
