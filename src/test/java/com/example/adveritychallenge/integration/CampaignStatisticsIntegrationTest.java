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

import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC27th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC28th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatOOC29th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl27th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl28th;
import static com.example.adveritychallenge.integration.DatabaseData.createCampaignDailyStatPickerl29th;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Sql(scripts = "classpath:db/fixtures.sql", config = @SqlConfig(encoding = "utf-8"))
public class CampaignStatisticsIntegrationTest extends BaseApiControllerTest {

    @Test
    void testReturnsDailyCampaignStatistics() throws Exception {
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
}
