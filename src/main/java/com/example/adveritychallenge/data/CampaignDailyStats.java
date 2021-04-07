package com.example.adveritychallenge.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class CampaignDailyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String campaign;

    private String datasource;

    private Long impressions;

    private Integer clicks;

    private LocalDate day;
}
