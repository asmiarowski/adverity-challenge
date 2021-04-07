package com.example.adveritychallenge.repository;

import com.example.adveritychallenge.data.CampaignDailyStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface CampaignDailyStatsRepository extends JpaRepository<CampaignDailyStats, Integer> {

    @Query(value = "FROM CampaignDailyStats cds WHERE cds.day >= :since AND cds.day <= :until")
    Page<CampaignDailyStats> findAllBetweenDates(
            @Param("since") LocalDate since,
            @Param("until") LocalDate until,
            Pageable pageable
    );
}
