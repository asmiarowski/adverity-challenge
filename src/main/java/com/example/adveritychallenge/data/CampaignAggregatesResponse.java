package com.example.adveritychallenge.data;

import lombok.Data;

@Data
public class CampaignAggregatesResponse {

    private String campaign;

    private String datasource;

    private Long views;

    private Integer clicks;
}
