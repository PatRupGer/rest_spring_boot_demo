package de.pat.rup.a.bk.models;

import java.sql.Date;

/**
 * Object representing the Request of the Rest Service
 */
public class Request {
    private final Date date;
    private final String baseCurrency;
    private final String targetCurrency;

    public Request(Date date, String baseCurrency, String targetCurrency) {
        this.date = date;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
    }

    public Date getDate() {
        return date;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }
}
