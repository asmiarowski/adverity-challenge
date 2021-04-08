package com.example.adveritychallenge.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class CampaignDailyStats {

    /**
     * Incremental generator is used to speed up batch insert of entries, however
     * because it keeps cache of highest ID value in memory it only works for single instance / thread
     * inserting data. If there would be a need to support multiple instances then Sequential generator with PostgreSQL
     * would be more appropriate.
     */
    @Id
    @GeneratedValue(generator = "incrementalGenerator")
    @GenericGenerator(name = "incrementalGenerator", strategy = "increment")
    @JsonIgnore
    private Integer id;

    private String campaign;

    private String datasource;

    private Long impressions;

    private Integer clicks;

    private LocalDate day;
}
