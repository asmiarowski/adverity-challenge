package com.example.adveritychallenge;

import com.example.adveritychallenge.data.CampaignDailyStats;

import java.time.LocalDate;
import java.util.List;

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
        stats.setDay(LocalDate.of(2019, 11, 12));
        stats.setClicks(7);
        stats.setImpressions(22425L);

        return stats;
    }

    public static List<CampaignDailyStats> createCampaignDailyStatsList() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 27));
        stats.setClicks(34);
        stats.setImpressions(813L);

        return List.of(createCampaignDailyStats(), stats);
    }
}
