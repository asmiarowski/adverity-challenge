package com.example.adveritychallenge.importer;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
@Validated
public class ImporterController {

    private final CampaignDailyStatsImporter importer;

    @GetMapping
    public ImporterResponse getStatsBetweenDates() throws IOException {
        return new ImporterResponse(importer.run());
    }
}
