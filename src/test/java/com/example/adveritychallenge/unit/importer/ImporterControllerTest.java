package com.example.adveritychallenge.unit.importer;

import com.example.adveritychallenge.BaseApiControllerTest;
import com.example.adveritychallenge.importer.CampaignDailyStatsImporter;
import com.example.adveritychallenge.importer.ImporterController;
import com.example.adveritychallenge.importer.ImporterResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = ImporterController.class)
public class ImporterControllerTest extends BaseApiControllerTest {

    @MockBean
    private CampaignDailyStatsImporter importer;

    @Test
    void testReturnsDailyCampaignStatisticsWithoutFiltering() throws Exception {
        when(importer.run()).thenReturn(true);

        ResultActions response = mockMvc.perform(get("/import"));

        verify(importer).run();
        validateSuccessJsonResponse(response, serializer.writeValueAsString(new ImporterResponse(true)));

        response.andDo(document("import"));
    }
}
