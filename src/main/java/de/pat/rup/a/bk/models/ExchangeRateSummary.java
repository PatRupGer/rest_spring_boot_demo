package de.pat.rup.a.bk.models;

import javax.persistence.*;
import java.sql.Date;

/**
 * Entity which will be persisted, holds all data required for RestService
 */
@Entity
public class ExchangeRateSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float exRate;
    private float averageExRate;
    @Enumerated(EnumType.STRING)
    private TrendType trendType;
    private Date date;

    public ExchangeRateSummary() {

    }

    public ExchangeRateSummary(float exRate, float averageExRate, TrendType trendType, Date date) {
        this.exRate = exRate;
        this.averageExRate = averageExRate;
        this.trendType = trendType;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getExRate() {
        return exRate;
    }

    public void setExRate(float exRate) {
        this.exRate = exRate;
    }

    public float getAverageExRate() {
        return averageExRate;
    }

    public void setAverageExRate(float averageExRate) {
        this.averageExRate = averageExRate;
    }

    public TrendType getTrendType() {
        return trendType;
    }

    public void setTrendType(TrendType trendType) {
        this.trendType = trendType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}


