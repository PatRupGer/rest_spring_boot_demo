package de.pat.rup.a.bk.service;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.repo.ExchangeRepoitory;
import de.pat.rup.a.bk.util.BKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExchangeService implements IExchangeService {
    @Autowired
    ExchangeRepoitory exchangeRepo;
    @Autowired
    private IRemoteRequester remoteRequester;
    @Autowired
    private CalculatorService serviceHelper;

    @Override
    public ExchangeRateSummary getExchangeRate(Request request) throws BKException {
        ExchangeRateSummary exchangeRateSummary = getExchangeRateSummary(request);
        // id, is added while saving
        exchangeRateSummary = exchangeRepo.save(exchangeRateSummary);
        return exchangeRateSummary;
    }

    @Override
    public List<ExchangeRateSummary> getExchangeRateHistoryDaily(Date date) {
        return exchangeRepo.daily(date);
    }

    @Override
    public List<ExchangeRateSummary> getExchangeRateHistoryMonthly(Date begin) {
//        Maybe not the most pretty solution
//        Will be used to make monthly query
        LocalDate helper = begin.toLocalDate();
        int year = helper.getYear();
        int month = helper.getMonthValue();
        if (month == 12) {
            year++;
        } else
            month++;
        Date end = Date.valueOf(year + "-" + month + "-01");
        return exchangeRepo.monthly(begin, end);
    }

    /*
     * Will build the ExchangeRateSummary Object
     */
    private ExchangeRateSummary getExchangeRateSummary(Request request) throws BKException {
        ExchangeRateSummary exchangeRateSummary = new ExchangeRateSummary();

        float exRate = remoteRequester.requestRemoteExchangeRate(request);
        // request 5 Days before request.date in reversed Order
        List<Float> ratesRevers = new ArrayList<>();
        ratesRevers.add(exRate);
        ratesRevers.addAll(remoteRequester.getFiveRatesReversedOrder(request));

        exchangeRateSummary.setAverageExRate(serviceHelper.calcAverage(ratesRevers));
        exchangeRateSummary.setTrendType(serviceHelper.evaluateTrend(ratesRevers));
        exchangeRateSummary.setDate(request.getDate());
        exchangeRateSummary.setExRate(exRate);
        return exchangeRateSummary;
    }

}
