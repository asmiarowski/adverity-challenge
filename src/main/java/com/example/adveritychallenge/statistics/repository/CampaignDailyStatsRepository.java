package com.example.adveritychallenge.statistics.repository;

import com.example.adveritychallenge.statistics.CampaignDailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignDailyStatsRepository extends JpaRepository<CampaignDailyStats, Integer>, CampaignDailyStatsRepositoryCustom {

}
