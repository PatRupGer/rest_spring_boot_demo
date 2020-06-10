package de.pat.rup.a.bk.service;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.util.BKException;

import java.sql.Date;
import java.util.List;

public interface IExchangeService {
    /**
     * Will request Exchangerates and save ExchangeRateSummary into Database
     *
     * @param request the request
     * @return a Summary Object for the Request
     * @throws BKException thrown when Currency is not known
     */
    ExchangeRateSummary getExchangeRate(Request request) throws BKException;

    /**
     * Get Historical Information on daily basis from Datatbase
     *
     * @param date the reqguested Date
     * @return all ExchangeRate which have been stored with specific Date
     */
    List<ExchangeRateSummary> getExchangeRateHistoryDaily(Date date);

    /**
     * Get Historical Information on monthly basis from Datatbase
     *
     * @param date the reqguested Date
     * @return all ExchangeRate which have been stored with specific Month
     */
    List<ExchangeRateSummary> getExchangeRateHistoryMonthly(Date date);
}