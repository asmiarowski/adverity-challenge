package com.example.adveritychallenge.service.importer;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.repository.CampaignDailyStatsRepository;
import com.example.adveritychallenge.service.importer.exceptions.ImportFileNotSpecifiedException;
import com.example.adveritychallenge.service.importer.lock.ImportLock;
import com.example.adveritychallenge.service.importer.lock.ImportLockRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
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

    @PersistenceContext
    private final EntityManager entityManager;

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

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size:30}")
    private int batchSize;

    public boolean run() throws IOException {
        if (importerProperties.getDailyCampaignFilePath().isEmpty()) {
            throw new ImportFileNotSpecifiedException();
        }

        log.info("Starting campaign daily stats import from csv file.");

        long startTime = System.nanoTime();

        var lock = importLockRepository.findByImporterType(CAMPAIGN_DAILY_STATS_CSV)
                .orElse(new ImportLock(CAMPAIGN_DAILY_STATS_CSV));

        if (lock.isLocked()) {
            log.info("Campaign daily stats already imported. Skipping.");
            return false;
        }

        var file = new File(importerProperties.getDailyCampaignFilePath());

        try (Reader reader = new FileReader(file)) {
            var objectReader = getObjectReader();
            var mappingIterator = objectReader.readValues(reader);
            while (mappingIterator.hasNext()) {
                readLine(mappingIterator);

                if (campaignDailyStatsList.size() >= batchSize) {
                    saveBulk();
                }
            }

            if (!campaignDailyStatsList.isEmpty()) {
                saveBulk();
            }
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        log.info("Finished importing campaign daily stats from CSV. Imported {} records, and {} records failed. Finished in {} s",
                successes, failures, (double)timeElapsed / 1000000000);

        cleanup();

        lock.setLocked(true);
        importLockRepository.save(lock);

        return true;
    }

    private ObjectReader getObjectReader() {
        var csvMapper = new CsvMapper();
        var schema = CsvSchema.emptySchema().withHeader();

        return csvMapper.readerFor(CampaignDailyStatsCsvLine.class).with(schema);
    }

    private void readLine(MappingIterator<Object> mappingIterator) {
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
        entityManager.clear();

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
