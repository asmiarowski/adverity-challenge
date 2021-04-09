package com.example.adveritychallenge;

import com.example.adveritychallenge.statistics.CampaignDailyStats;
import com.example.adveritychallenge.statistics.CampaignStatsAggregate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.adveritychallenge.statistics.DailyStatsGroupBy.CAMPAIGN;

/**
 * The purpose of this class is to prepare example data objects to be used during unit testing.
 * These values will be used when generating API documentation snippets, so it's beneficial to have them as close to
 * real data as possible.
 */
public class TestData {

    public static CampaignDailyStats createCampaignDailyStats() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Adventmarkt Touristik");
        stats.setCampaign("Google Ads");
        stats.setDay(LocalDate.of(2019, 10, 20));
        stats.setClicks(7);
        stats.setImpressions(22425L);

        return stats;
    }

    public static List<CampaignDailyStats> createCampaignDailyStatsList() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 10, 27));
        stats.setClicks(34);
        stats.setImpressions(813L);

        return List.of(createCampaignDailyStats(), stats);
    }

    public static List<CampaignStatsAggregate> createCampaignAggregatedStatsList() {
        var stats = new CampaignStatsAggregate();
        stats.setGroupedBy(CAMPAIGN);
        stats.setGroupedByValue("Adventmarkt Touristik");
        stats.setClicks(7L);
        stats.setImpressions(22425L);
        stats.setCtr(BigDecimal.valueOf(0.0));

        var stats2 = new CampaignStatsAggregate();
        stats2.setGroupedBy(CAMPAIGN);
        stats2.setGroupedByValue("Pickerl-Erinnerung");
        stats2.setClicks(34L);
        stats2.setImpressions(813L);
        stats2.setCtr(BigDecimal.valueOf(4.2));

        return List.of(stats);
    }
}
