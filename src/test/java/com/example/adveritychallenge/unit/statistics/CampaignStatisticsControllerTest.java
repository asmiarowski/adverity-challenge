package com.example.adveritychallenge.unit.statistics;

import com.example.adveritychallenge.TestData;
import com.example.adveritychallenge.BaseApiControllerTest;
import com.example.adveritychallenge.statistics.CampaignStatisticsController;
import com.example.adveritychallenge.statistics.CampaignStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_CAMPAIGN;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_CLICKS;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_CONTENT;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_CTR;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_DATASOURCE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_DAY;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_GROUPED_BY;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_GROUPED_BY_VALUE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_IMPRESSIONS;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_NUMBER;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_SIZE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_TOTAL_ELEMENTS;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.FIELD_TOTAL_PAGES;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_CAMPAIGN_FILTERS;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_DATASOURCE_FILTERS;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_GROUP_BY;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_PAGE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_SINCE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_SIZE;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_SORT_AGGREGATED;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_SORT_DAILY;
import static com.example.adveritychallenge.unit.statistics.CampaignStatisticsControllerTest.Documentation.PARAMETER_UNTIL;
import static com.example.adveritychallenge.statistics.DailyStatsGroupBy.CAMPAIGN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = CampaignStatisticsController.class)
public class CampaignStatisticsControllerTest extends BaseApiControllerTest {

    @MockBean
    private CampaignStatsService service;

    @Test
    void testReturnsDailyCampaignStatistics() throws Exception {
        var dateSince = LocalDate.of(2019, 10, 19);
        var dateUntil = LocalDate.of(2019, 11, 20);
        var pageable = PageRequest.of(1, 20, Sort.by(ASC, "day"));
        var resultPage = new PageImpl<>(TestData.createCampaignDailyStatsList());

        when(service.getStatsBetweenDates(any(), any(), any(), any(), any())).thenReturn(resultPage);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/daily")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("page", "1")
                .param("sort", "day,asc")
                .param("campaignFilters", "Pickerl-Erinnerung", "Adventmarkt Touristik")
                .param("datasourceFilters", "Twitter Ads", "Google Ads")
        );

        verify(service).getStatsBetweenDates(dateSince, dateUntil, List.of("Pickerl-Erinnerung", "Adventmarkt Touristik"),
                List.of("Twitter Ads", "Google Ads"), pageable);
        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));

        response.andDo(document("campaign-statistics/daily",
                requestParameters(PARAMETER_PAGE, PARAMETER_SIZE, PARAMETER_SORT_DAILY, PARAMETER_SINCE, PARAMETER_UNTIL,
                        PARAMETER_CAMPAIGN_FILTERS, PARAMETER_DATASOURCE_FILTERS
                ),
                relaxedResponseFields(FIELD_TOTAL_PAGES, FIELD_TOTAL_ELEMENTS, FIELD_NUMBER, FIELD_SIZE, FIELD_CONTENT)
                        .andWithPrefix("content[].", List.of(FIELD_CAMPAIGN, FIELD_DATASOURCE, FIELD_IMPRESSIONS,
                                FIELD_CLICKS, FIELD_CTR, FIELD_DAY))
        ));
    }

    @Test
    void testReturnsAggregatedCampaignStatisticsWithoutFiltering() throws Exception {
        var dateSince = LocalDate.of(2019, 10, 19);
        var dateUntil = LocalDate.of(2019, 11, 20);
        var pageable = PageRequest.of(1, 20);
        var resultPage = new PageImpl<>(TestData.createCampaignAggregatedStatsList());

        when(service.getAggregatedStatsBetweenDates(any(), any(), any(), any(), any(), any())).thenReturn(resultPage);

        ResultActions response = mockMvc.perform(get("/campaign-statistics/aggregated")
                .param("since", dateSince.toString())
                .param("until", dateUntil.toString())
                .param("groupBy", "campaign")
                .param("page", "1")
        );

        verify(service).getAggregatedStatsBetweenDates(dateSince, dateUntil, List.of(), List.of(), CAMPAIGN, pageable);
        validateSuccessJsonResponse(response, serializer.writeValueAsString(resultPage));

        response.andDo(document("campaign-statistics/aggregated",
                requestParameters(PARAMETER_PAGE, PARAMETER_SIZE, PARAMETER_SORT_AGGREGATED, PARAMETER_SINCE, PARAMETER_UNTIL,
                        PARAMETER_CAMPAIGN_FILTERS, PARAMETER_DATASOURCE_FILTERS, PARAMETER_GROUP_BY
                ),
                relaxedResponseFields(FIELD_TOTAL_PAGES, FIELD_TOTAL_ELEMENTS, FIELD_NUMBER, FIELD_SIZE, FIELD_CONTENT)
                        .andWithPrefix("content[].", List.of(FIELD_GROUPED_BY, FIELD_GROUPED_BY_VALUE,
                                FIELD_IMPRESSIONS, FIELD_CLICKS, FIELD_CTR))
        ));
    }

    static class Documentation {
        static final ParameterDescriptor PARAMETER_PAGE = parameterWithName("page")
                .optional()
                .description("The page to retrieve. Starts from 0.");
        static final ParameterDescriptor PARAMETER_SIZE = parameterWithName("size")
                .optional()
                .description("How many records should be returned per page.");
        static final ParameterDescriptor PARAMETER_SORT_DAILY = parameterWithName("sort")
                .optional()
                .description("Sort by property and direction. Accepted format is 'property,direction'. It's possible to provide multiple sort parameters for sorting by multiple properties. Available sortable properties are: campaign, datasource, day, clicks, impressions, ctr");
        static final ParameterDescriptor PARAMETER_SORT_AGGREGATED = parameterWithName("sort")
                .optional()
                .description("Sort by property and direction. Accepted format is 'property,direction'. It's possible to provide multiple sort parameters for sorting by multiple properties. Available sortable properties are: groupedByValue, clicks, impressions, ctr");
        static final ParameterDescriptor PARAMETER_SINCE = parameterWithName("since")
                .description("Date since to return list of matching statistics for campaigns. Inclusive. Date format in YYYY-MM-DD.");
        static final ParameterDescriptor PARAMETER_UNTIL = parameterWithName("until")
                .description("Date until to return list of matching statistics for campaigns. Inclusive. Date format in YYYY-MM-DD.");
        static final ParameterDescriptor PARAMETER_CAMPAIGN_FILTERS = parameterWithName("campaignFilters")
                .optional()
                .description("Filters resulting statistics to specified list of campaigns.");
        static final ParameterDescriptor PARAMETER_DATASOURCE_FILTERS = parameterWithName("datasourceFilters")
                .optional()
                .description("Filters resulting statistics to specified list of data sources.");
        static final ParameterDescriptor PARAMETER_GROUP_BY = parameterWithName("groupBy")
                .description("Groups by values of selected property. Allowed values are 'campaign' and 'datasource'");

        static final FieldDescriptor FIELD_CONTENT = fieldWithPath("content[]").description("List of daily campaign statistics. Zerofill in missing dates should be done on the Client side if such presentation is necessary.");

        static final FieldDescriptor FIELD_CAMPAIGN = fieldWithPath("campaign").description("Name of the campaign this statistics apply to.");
        static final FieldDescriptor FIELD_DATASOURCE = fieldWithPath("datasource").description("Name of the data source this statistics come from.");
        static final FieldDescriptor FIELD_IMPRESSIONS = fieldWithPath("impressions").description("Count of the ad views.");
        static final FieldDescriptor FIELD_CLICKS = fieldWithPath("clicks").description("Count of the ad clicks.");
        static final FieldDescriptor FIELD_DAY = fieldWithPath("day").description("The day this statistics apply to.");
        static final FieldDescriptor FIELD_CTR = fieldWithPath("ctr").description("Percentage based CTR (click-through-rate) with precision 1. Example value 14.5 is equal 14.5%");

        static final FieldDescriptor FIELD_TOTAL_PAGES = fieldWithPath("totalPages").description("How many pages are available for request with selected parameters.");
        static final FieldDescriptor FIELD_TOTAL_ELEMENTS = fieldWithPath("totalElements").description("How many records are available on all pages for request with selected parameters.");
        static final FieldDescriptor FIELD_NUMBER = fieldWithPath("number").description("Currently selected page");
        static final FieldDescriptor FIELD_SIZE = fieldWithPath("size").description("Currently selected page size.");

        static final FieldDescriptor FIELD_GROUPED_BY = fieldWithPath("groupedBy").description("This property corresponds to groupBy parameter send in request.");
        static final FieldDescriptor FIELD_GROUPED_BY_VALUE = fieldWithPath("groupedByValue").description("This is the value for matched groupBy. When grouping by campaign, this would be the value from campaign property, for example 'Adventmarkt Touristik'.");

    }
}
