package com.example.adveritychallenge.data;

import lombok.Data;

@Data
public class ClickThroughRateResponse {

    private String campaign;

    private String datasource;

    private Double ctr;
}
