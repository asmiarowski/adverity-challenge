package com.example.adveritychallenge.repository;

import com.example.adveritychallenge.data.CampaignDailyStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignDailyStatsRepository extends JpaRepository<CampaignDailyStats, Integer>, CampaignDailyStatsRepositoryCustom {

}
