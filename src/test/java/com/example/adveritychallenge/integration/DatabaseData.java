package com.example.adveritychallenge.integration;

import com.example.adveritychallenge.data.CampaignDailyStats;

import java.time.LocalDate;

/**
 * The purpose of this class is to quickly prepare objects that equal data inserted to database in
 * resources/db/fixtures.sql
 */
public class DatabaseData {

    public static CampaignDailyStats createCampaignDailyStatPickerl27th() {
        var stats = new CampaignDailyStats();
        stats.setId(1);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 27));
        stats.setClicks(34);
        stats.setImpressions(813L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatPickerl28th() {
        var stats = new CampaignDailyStats();
        stats.setId(2);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 28));
        stats.setClicks(27);
        stats.setImpressions(796L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatPickerl29th() {
        var stats = new CampaignDailyStats();
        stats.setId(3);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 29));
        stats.setClicks(69);
        stats.setImpressions(1473L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC27th() {
        var stats = new CampaignDailyStats();
        stats.setId(4);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 27));
        stats.setClicks(15);
        stats.setImpressions(212L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC28th() {
        var stats = new CampaignDailyStats();
        stats.setId(5);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 28));
        stats.setClicks(25);
        stats.setImpressions(265L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC29th() {
        var stats = new CampaignDailyStats();
        stats.setId(6);
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 29));
        stats.setClicks(26);
        stats.setImpressions(319L);

        return stats;
    }
}
