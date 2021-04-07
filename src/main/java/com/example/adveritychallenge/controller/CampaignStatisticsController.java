package com.example.adveritychallenge.controller;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.service.statistics.CampaignStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/campaign-statistics")
@RequiredArgsConstructor
@Validated
public class CampaignStatisticsController {

    private final CampaignStatsService service;

    @RequestMapping(path = "/daily")
    public Page<CampaignDailyStats> getDaily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until,
                                             Pageable pageable) {
        var result = service.getDaily(since, until, pageable);

        return result;
    }
}
