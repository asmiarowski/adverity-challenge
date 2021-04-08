package com.example.adveritychallenge.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignStatsAggregate {

    /**
     * This constructor is needed because Hibernate criteria API doesn't want to work with enums, hence we apply string.
     * It's used by CampaignDailyStatsRepositoryCustomImpl class, even though IDE doesn't pick it up.
     */
    public CampaignStatsAggregate(String groupBy, String groupedByValue, long impressions, long clicks) {
        this.groupedBy = DailyStatsGroupBy.getEnum(groupBy);
        this.groupedByValue = groupedByValue;
        this.impressions = impressions;
        this.clicks = clicks;
    }

    private DailyStatsGroupBy groupedBy;

    private String groupedByValue;

    private long impressions;

    private long clicks;
}
