package com.example.adveritychallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
public abstract class BaseApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper serializer;

    /**
     * This method validates proper response has been given by the mock MVC instance.
     *
     * @param mockResponse Result of mockMvc.perform()
     * @param response     JSON response that should be returned from the request
     */
    protected void validateSuccessJsonResponse(ResultActions mockResponse, String response) throws Exception {
        if (response != null) {
            mockResponse.andExpect(content().json(response));
        }

        mockResponse.andExpect(status().isOk());
    }
}
