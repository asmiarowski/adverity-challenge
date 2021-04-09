package com.example.adveritychallenge.statistics.repository;

import com.example.adveritychallenge.statistics.CampaignDailyStats;
import com.example.adveritychallenge.statistics.CampaignStatsAggregate;
import com.example.adveritychallenge.statistics.DailyStatsGroupBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

interface CampaignDailyStatsRepositoryCustom {
    Page<CampaignDailyStats> findAllBetweenDates(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            Pageable pageable
    );

    Page<CampaignStatsAggregate> findAllBetweenDatesGroupedBy(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            DailyStatsGroupBy groupBy,
            Pageable pageable
    );
}
