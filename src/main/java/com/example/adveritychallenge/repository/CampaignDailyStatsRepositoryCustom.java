package com.example.adveritychallenge.repository;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.data.CampaignStatsAggregate;
import com.example.adveritychallenge.data.DailyStatsGroupBy;
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
