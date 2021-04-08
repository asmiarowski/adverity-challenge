package com.example.adveritychallenge.integration.importer;

import com.example.adveritychallenge.integration.DatabaseData;
import com.example.adveritychallenge.repository.CampaignDailyStatsRepository;
import com.example.adveritychallenge.service.importer.CampaignDailyStatsImporter;
import com.example.adveritychallenge.service.importer.ImporterProperties;
import com.example.adveritychallenge.service.importer.lock.ImportLock;
import com.example.adveritychallenge.service.importer.lock.ImportLockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.example.adveritychallenge.service.importer.lock.ImporterType.CAMPAIGN_DAILY_STATS_CSV;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:db/cleanup.sql")
public class CampaignDailyStatsImporterIntegrationTest {

    @Autowired
    private CampaignDailyStatsImporter importer;

    @Autowired
    private CampaignDailyStatsRepository statsRepository;

    @Autowired
    private ImportLockRepository lockRepository;

    @Autowired
    private ImporterProperties importerProperties;

    @Test
    void testImportsCsvWithDailyCampaignStats() {
        var expectedLock = new ImportLock(CAMPAIGN_DAILY_STATS_CSV);
        expectedLock.setId(1);
        expectedLock.setLocked(true);
        var expectedExampleEntry = DatabaseData.createCampaignDailyStatPickerl27th();
        var date = expectedExampleEntry.getDay();
        importerProperties.setDailyCampaignFilePath("src/test/resources/files/campaign-daily-stats.csv");

        importer.run();

        assertThat(statsRepository.count()).isEqualTo(8);
        var exampleEntry = statsRepository.findAllBetweenDates(date, date, List.of(), List.of(),
                PageRequest.of(0, 1)).getContent();
        assertThat(exampleEntry).usingRecursiveComparison().ignoringFields("id").isEqualTo(List.of(expectedExampleEntry));
        assertThat(lockRepository.findByImporterType(CAMPAIGN_DAILY_STATS_CSV)).isEqualTo(Optional.of(expectedLock));
    }
}
