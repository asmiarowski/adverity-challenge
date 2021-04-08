package com.example.adveritychallenge.integration;

import com.example.adveritychallenge.controller.BaseApiControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.example.adveritychallenge.data.DailyStatsGroupBy.CAMPAIGN;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC27th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC28th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC29th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl27th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl28th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl29th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignGroupedStatsOOC;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignGroupedStatsPickerl;
import static com.example.adveritychallenge.integration.DatabaseData.createDatasourceGroupedStatsGoogle;
import static com.example.adveritychallenge.integration.DatabaseData.createDatasourceGroupedStatsTwitter;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Sql(scripts = "classpath:db/fixtures.sql", config = @SqlConfig(encoding = "utf-8"))
public class CampaignStatisticsIntegrationTest extends BaseApiControllerTest {

    @Test
    void testReturnsDailyCampaignStatisticsWithoutFilters() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createCampaignDailyStatPickerl27th(),
                createCampaignDailyStatPickerl28th(),
                createCampaignDailyStatPickerl29th(),
                createCampaignDailyStatOOC27th(),
                createCampaignDailyStatOOC28th(),
                createCampaignDailyStatOOC29th()
        ), pageable, 6);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsDailyCampaignStatisticsWithoutFiltersSingleDay() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 27);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createCampaignDailyStatPickerl27th(),
                createCampaignDailyStatOOC27th()
        ), pageable, 2);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsDailyCampaignStatisticsWithCampaignFilter() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createCampaignDailyStatOOC27th(),
                createCampaignDailyStatOOC28th(),
                createCampaignDailyStatOOC29th()
        ), pageable, 3);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("campaignFilters", resultPage.getContent().get(0).getCampaign())
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsDailyCampaignStatisticsWithDatasourceFilter() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createCampaignDailyStatOOC29th()
        ), pageable, 1);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("datasourceFilters", "Google Ads")
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsCampaignStatisticsGroupedByCampaign() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createCampaignGroupedStatsPickerl(),
                createCampaignGroupedStatsOOC()
        ), pageable, 2);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/aggregated")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("groupBy", "campaign")
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsCampaignStatisticsGroupedByDatasource() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(
                createDatasourceGroupedStatsTwitter(),
                createDatasourceGroupedStatsGoogle()
        ), pageable, 2);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/aggregated")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("groupBy", "datasource")
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsCampaignStatisticsGroupedByCampaignAndFilteredByCampaign() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var resultPage = new PageImpl<>(List.of(createCampaignGroupedStatsPickerl()), pageable, 1);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/aggregated")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("groupBy", "campaign")
                .param("campaignFilters", "Pickerl-Erinnerung")
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsCampaignStatisticsGroupedByCampaignAndFilteredByCampaignAndDatasource() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(0, 20);
        var aggregatedResult = createDatasourceGroupedStatsGoogle();
        aggregatedResult.setGroupedBy(CAMPAIGN);
        aggregatedResult.setGroupedByValue("ÖCC");
        var resultPage = new PageImpl<>(List.of(aggregatedResult), pageable, 1);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/aggregated")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("groupBy", "campaign")
                .param("campaignFilters", "ÖCC")
                .param("datasourceFilters", "Google Ads")
                .param("page", "0")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }

    @Test
    void testReturnsDailyCampaignStatisticsSecondPage() throws Exception {
        var dateSince = LocalDate.of(2019, 4, 27);
        var dateUntil = LocalDate.of(2019, 4, 29);
        var pageable = PageRequest.of(1, 3);
        var resultPage = new PageImpl<>(List.of(
                createCampaignDailyStatOOC27th(),
                createCampaignDailyStatOOC28th(),
                createCampaignDailyStatOOC29th()
        ), pageable, 6);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("page", "1")
                .param("size", "3")
        );

        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));
    }
}
