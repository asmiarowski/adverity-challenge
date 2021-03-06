package com.example.adveritychallenge.unit.statistics;

import com.example.adveritychallenge.TestData;
import com.example.adveritychallenge.statistics.CampaignStatsService;
import com.example.adveritychallenge.statistics.repository.CampaignDailyStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampaignStatsServiceTest {

    @Mock
    private CampaignDailyStatsRepository repository;

    @InjectMocks
    private CampaignStatsService service;

    @Test
    void testReturnsPageOfDailyStatisticsWithoutFiltering() {
        var dateSince = LocalDate.of(2019, 10, 19);
        var dateUntil = LocalDate.of(2019, 11, 20);
        var pageable = PageRequest.of(1, 20);
        var fetchedPage = new PageImpl<>(TestData.createCampaignDailyStatsList());

        when(repository.findAllBetweenDates(any(), any(), any(), any(), any())).thenReturn(fetchedPage);

        var resultPage = service.getStatsBetweenDates(dateSince, dateUntil, List.of(), List.of(), pageable);

        verify(repository).findAllBetweenDates(dateSince, dateUntil, List.of(), List.of(), pageable);
        assertThat(resultPage).isEqualTo(fetchedPage);
    }
}
