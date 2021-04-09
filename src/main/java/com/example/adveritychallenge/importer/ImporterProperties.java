package com.example.adveritychallenge.importer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "com.example.importer")
public class ImporterProperties {
    private String dailyCampaignFilePath;
}
