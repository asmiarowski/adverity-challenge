package com.example.adveritychallenge.service.statistics;

import com.example.adveritychallenge.data.DailyStatsGroupBy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DailyStatsGroupByConverter implements Converter<String, DailyStatsGroupBy> {

    @Override
    public DailyStatsGroupBy convert(String source) {
        return DailyStatsGroupBy.valueOf(source.toUpperCase());
    }
}
