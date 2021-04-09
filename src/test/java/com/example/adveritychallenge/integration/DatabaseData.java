package com.example.adveritychallenge.integration;

import com.example.adveritychallenge.statistics.CampaignDailyStats;
import com.example.adveritychallenge.statistics.CampaignStatsAggregate;
import com.example.adveritychallenge.statistics.DailyStatsGroupBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * The purpose of this class is to quickly prepare objects that equal data inserted to database in
 * resources/db/fixtures.sql
 */
public class DatabaseData {

    public static CampaignDailyStats createCampaignDailyStatPickerl27th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 27));
        stats.setClicks(34);
        stats.setImpressions(813L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatPickerl28th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 28));
        stats.setClicks(27);
        stats.setImpressions(796L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatPickerl29th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("Pickerl-Erinnerung");
        stats.setDay(LocalDate.of(2019, 4, 29));
        stats.setClicks(69);
        stats.setImpressions(1473L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC27th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 27));
        stats.setClicks(15);
        stats.setImpressions(212L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC28th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Twitter Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 28));
        stats.setClicks(25);
        stats.setImpressions(265L);

        return stats;
    }

    public static CampaignDailyStats createCampaignDailyStatOOC29th() {
        var stats = new CampaignDailyStats();
        stats.setDatasource("Google Ads");
        stats.setCampaign("ÖCC");
        stats.setDay(LocalDate.of(2019, 4, 29));
        stats.setClicks(26);
        stats.setImpressions(319L);

        return stats;
    }

    public static CampaignStatsAggregate createCampaignGroupedStatsOOC() {
        var aggregated = new CampaignStatsAggregate();
        var stats1 = createCampaignDailyStatOOC27th();
        var stats2 = createCampaignDailyStatOOC28th();
        var stats3 = createCampaignDailyStatOOC29th();

        aggregated.setImpressions(stats1.getImpressions() + stats2.getImpressions() + stats3.getImpressions());
        aggregated.setClicks((long) stats1.getClicks() + stats2.getClicks() + stats3.getClicks());
        aggregated.setGroupedBy(DailyStatsGroupBy.CAMPAIGN);
        aggregated.setGroupedByValue(stats1.getCampaign());
        var ctr = BigDecimal.valueOf((double) aggregated.getClicks() / (double) aggregated.getImpressions() * 100.0)
                .setScale(1, RoundingMode.HALF_UP);
        aggregated.setCtr(ctr);

        return aggregated;
    }

    public static CampaignStatsAggregate createCampaignGroupedStatsPickerl() {
        var aggregated = new CampaignStatsAggregate();
        var stats1 = createCampaignDailyStatPickerl27th();
        var stats2 = createCampaignDailyStatPickerl28th();
        var stats3 = createCampaignDailyStatPickerl29th();

        aggregated.setImpressions(stats1.getImpressions() + stats2.getImpressions() + stats3.getImpressions());
        aggregated.setClicks((long) stats1.getClicks() + stats2.getClicks() + stats3.getClicks());
        aggregated.setGroupedBy(DailyStatsGroupBy.CAMPAIGN);
        aggregated.setGroupedByValue(stats1.getCampaign());
        var ctr = BigDecimal.valueOf((double) aggregated.getClicks() / (double) aggregated.getImpressions() * 100.0)
                .setScale(1, RoundingMode.HALF_UP);
        aggregated.setCtr(ctr);

        return aggregated;
    }

    public static CampaignStatsAggregate createDatasourceGroupedStatsTwitter() {
        var aggregated = new CampaignStatsAggregate();
        var stats1 = createCampaignDailyStatPickerl27th();
        var stats2 = createCampaignDailyStatPickerl28th();
        var stats3 = createCampaignDailyStatPickerl29th();
        var stats4 = createCampaignDailyStatOOC27th();
        var stats5 = createCampaignDailyStatOOC28th();

        aggregated.setImpressions(stats1.getImpressions() + stats2.getImpressions() + stats3.getImpressions() +
                stats4.getImpressions() + stats5.getImpressions());
        aggregated.setClicks((long) stats1.getClicks() + stats2.getClicks() + stats3.getClicks() + stats4.getClicks() +
                stats5.getClicks());
        aggregated.setGroupedBy(DailyStatsGroupBy.DATASOURCE);
        aggregated.setGroupedByValue(stats1.getDatasource());
        var ctr = BigDecimal.valueOf((double) aggregated.getClicks() / (double) aggregated.getImpressions() * 100.0)
                .setScale(1, RoundingMode.HALF_UP);
        aggregated.setCtr(ctr);

        return aggregated;
    }

    public static CampaignStatsAggregate createDatasourceGroupedStatsGoogle() {
        var aggregated = new CampaignStatsAggregate();
        var stats = createCampaignDailyStatOOC29th();

        aggregated.setImpressions(stats.getImpressions());
        aggregated.setClicks((long) stats.getClicks());
        aggregated.setGroupedBy(DailyStatsGroupBy.DATASOURCE);
        aggregated.setGroupedByValue(stats.getDatasource());
        var ctr = BigDecimal.valueOf((double) aggregated.getClicks() / (double) aggregated.getImpressions() * 100.0)
                .setScale(1, RoundingMode.HALF_UP);
        aggregated.setCtr(ctr);

        return aggregated;
    }
}
