package com.example.adveritychallenge.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/campaign-statistics")
@RequiredArgsConstructor
@Validated
public class CampaignStatisticsController {

    private final CampaignStatsService service;

    @RequestMapping("/daily")
    public Page<CampaignDailyStats> getStatsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until,
            @RequestParam(required = false, defaultValue = "") List<String> campaignFilters,
            @RequestParam(required = false, defaultValue = "") List<String> datasourceFilters,
            Pageable pageable
    ) {
        return service.getStatsBetweenDates(since, until, campaignFilters, datasourceFilters, pageable);
    }

    @RequestMapping("/aggregated")
    public Page<CampaignStatsAggregate> getStatsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until,
            @RequestParam(required = false, defaultValue = "") List<String> campaignFilters,
            @RequestParam(required = false, defaultValue = "") List<String> datasourceFilters,
            @RequestParam DailyStatsGroupBy groupBy,
            Pageable pageable
    ) {
        return service.getAggregatedStatsBetweenDates(since, until, campaignFilters, datasourceFilters, groupBy, pageable);
    }
}
