package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "quarter")
    private String quarter;

    @Basic
    @Column(name = "stock")
    private String stock;

    @Basic
    @Column(name = "date")
    private Date date;

    @Basic
    @Column(name = "open")
    private String open;

    @Basic
    @Column(name = "high")
    private String high;

    @Basic
    @Column(name = "low")
    private String low;

    @Basic
    @Column(name = "close")
    private String close;

    @Basic
    @Column(name = "volume")
    private long volume;

    @Basic
    @Column(name = "percentage_change_price")
    private Double percentageChangePrice;

    @Basic
    @Column(name = "percent_change_volume_over_last_wk")
    private Double percentChangeVolumeOverLastWeek;

    @Basic
    @Column(name = "previous_weeks_volume")
    private Double previousWeeksVolume;

    @Basic
    @Column(name = "next_weeks_open")
    private String nextWeeksOpen;

    @Basic
    @Column(name = "next_weeks_close")
    private String nextWeeksClose;

    @Basic
    @Column(name = "percent_change_next_weeks_price")
    private Double percentChangeNextWeeksPrice;

    @Basic
    @Column(name = "days_to_next_dividend")
    private int daysToNextDividend;

    @Basic
    @Column(name = "percent_return_next_dividend")
    private Double percentReturnNextDividend;
}
