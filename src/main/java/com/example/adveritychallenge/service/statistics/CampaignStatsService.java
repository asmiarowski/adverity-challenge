package com.example.adveritychallenge.service.statistics;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.repository.CampaignDailyStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
    public Page<CampaignDailyStats> getDaily(LocalDate since, LocalDate until, Pageable pageable) {
        return repository.findAllBetweenDates(since, until, pageable);
    }
}
