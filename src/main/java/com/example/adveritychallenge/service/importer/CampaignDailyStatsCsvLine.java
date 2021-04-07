package com.example.adveritychallenge.service.importer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CampaignDailyStatsCsvLine {

    @JsonProperty("Campaign")
    private String campaign;

    @JsonProperty("Datasource")
    private String datasource;

    @JsonProperty("Impressions")
    private Long impressions;

    @JsonProperty("Clicks")
    private Integer clicks;

    @JsonProperty("Daily")
    private String daily;

}
