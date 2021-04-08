package com.example.adveritychallenge.service.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/importer")
@RequiredArgsConstructor
@Validated
public class ImporterController {

    private final CampaignDailyStatsImporter importer;

    @RequestMapping("/refresh")
    public LockResponse getStatsBetweenDates() throws IOException {
        return new LockResponse(importer.run());
    }
}
