package com.example.adveritychallenge.statistics;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DailyStatsGroupBy {
    CAMPAIGN("campaign"),
    DATASOURCE("datasource");

    final private String value;

    DailyStatsGroupBy(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static DailyStatsGroupBy getEnum(String value) {
        for (var val : values()) {
            if (val.getValue().equalsIgnoreCase(value)) {
                return val;
            }
        }
        throw new IllegalArgumentException();
    }
}
