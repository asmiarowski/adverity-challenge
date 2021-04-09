package com.example.adveritychallenge.statistics;

import com.example.adveritychallenge.statistics.repository.CampaignDailyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignStatsService {

    private final CampaignDailyStatsRepository repository;

    /**
     * Fetches clicks and views aggregates between selected dates.
     * To fetch single date, both since and until should point to it.
     *
     * @param since Starting date of the list of daily statistics to fetch - inclusive.
     * @param until Last date of the list of daily statistics to fetch - inclusive.
     * @param pageable Pagination object
     */
    public Page<CampaignDailyStats> getStatsBetweenDates(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            Pageable pageable
    ) {
        return repository.findAllBetweenDates(since, until, campaignFilters, datasourceFilters, pageable);
    }

    public Page<CampaignStatsAggregate> getAggregatedStatsBetweenDates(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            DailyStatsGroupBy groupBy,
            Pageable pageable
    ) {
        return repository.findAllBetweenDatesGroupedBy(since, until, campaignFilters, datasourceFilters, groupBy, pageable);
    }
}
