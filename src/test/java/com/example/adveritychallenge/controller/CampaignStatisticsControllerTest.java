package com.example.adveritychallenge.controller;

import com.example.adveritychallenge.TestData;
import com.example.adveritychallenge.service.statistics.CampaignStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = CampaignStatisticsController.class)
public class CampaignStatisticsControllerTest extends BaseApiControllerTest {

    @MockBean
    private CampaignStatsService service;

    @Test
    void testReturnsDailyCampaignStatistics() throws Exception {
        var dateSince = LocalDate.of(2019, 10, 19);
        var dateUntil = LocalDate.of(2019, 11, 20);
        var pageable = PageRequest.of(1, 20);
        var resultPage = new PageImpl<>(TestData.createCampaignDailyStatsList());

        when(service.getDaily(any(), any(), any())).thenReturn(resultPage);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("page", "1")
        );

        verify(service).getDaily(dateSince, dateUntil, pageable);
        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));

        response.andDo(document("campaign-statistics/daily"));
    }
}
