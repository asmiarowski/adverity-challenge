package com.example.adveritychallenge.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignStatsAggregate {

    /**
     * This constructor is needed because Hibernate criteria API doesn't want to work with enums, hence we apply string.
     * It's used by CampaignDailyStatsRepositoryCustomImpl class, even though IDE doesn't pick it up.
     *
     * @param ctr CTR is expected to be in percentage multiplied by 10 to resolve MySQL rounding issue. See CampaignDailyStatsRepositoryCustomImpl::findAllBetweenDatesGroupedBy
     */
    public CampaignStatsAggregate(String groupBy, String groupedByValue, long impressions, long clicks, Double ctr) {
        this.groupedBy = DailyStatsGroupBy.getEnum(groupBy);
        this.groupedByValue = groupedByValue;
        this.impressions = impressions;
        this.clicks = clicks;
        this.ctr = BigDecimal.valueOf(ctr / 10.0).setScale(1, RoundingMode.HALF_UP);
    }

    private DailyStatsGroupBy groupedBy;

    private String groupedByValue;

    private long impressions;

    private long clicks;

    private BigDecimal ctr;
}
