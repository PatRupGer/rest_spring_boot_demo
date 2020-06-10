package de.pat.rup.a.bk.controller;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.service.IExchangeService;
import de.pat.rup.a.bk.util.BKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static de.pat.rup.a.bk.util.FINALS.FIRSTDATE;
import static de.pat.rup.a.bk.util.FINALS.LASTDATE;

@Controller
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {
    @Autowired
    IExchangeService exchangeService;

    @GetMapping("/{date}/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<ExchangeRateSummary> getExchangeRateSummary(@PathVariable("date") String date,
                                                                      @PathVariable("baseCurrency") String baseCurrency,
                                                                      @PathVariable("targetCurrency") String targetCurrency) throws BKException {
        checkDate(date);
        Request request = new Request(Date.valueOf(date), baseCurrency, targetCurrency);
        ExchangeRateSummary exchangeRateSummary = exchangeService.getExchangeRate(request);
        return new ResponseEntity<ExchangeRateSummary>(exchangeRateSummary, HttpStatus.OK);
    }

    @GetMapping("/history/daily/{year}/{month}/{day}")
    public ResponseEntity<List<ExchangeRateSummary>> getDailyHistory(@PathVariable("year") String year,
                                                                     @PathVariable("month") String month,
                                                                     @PathVariable("day") String day) throws BKException {
        String sDate = year + "-" + month + "-" + day;
        checkDate(sDate);
        Date temp = Date.valueOf(sDate);
        return new ResponseEntity<List<ExchangeRateSummary>>(exchangeService.getExchangeRateHistoryDaily(temp), HttpStatus.OK);
    }

    @GetMapping("/history/monthly/{year}/{month}")
    public ResponseEntity<List<ExchangeRateSummary>> getMonthlyHistory(@PathVariable("year") String year,
                                                                       @PathVariable("month") String month) throws BKException {
        // just asume it will be the first
        String sDate = year + "-" + month + "-" + "01";
        checkDate(sDate);
        Date temp = Date.valueOf(sDate);
        return new ResponseEntity<List<ExchangeRateSummary>>(exchangeService.getExchangeRateHistoryMonthly(temp), HttpStatus.OK);
    }

    /*
     * Check if Date is after FIRSTDATE or before LASTDATE
     * Check that date is formed properly
     */
    private void checkDate(String date) throws BKException {
        try {
            LocalDate dateTest = LocalDate.parse(date);
            if (dateTest.isBefore(FIRSTDATE) || dateTest.isAfter(LASTDATE)) {
                throw new BKException("You are not allowed to use Dates before " + FIRSTDATE
                        + " of after " + LASTDATE);
            }
        } catch (DateTimeParseException e) {
            throw new BKException("Date is not formed correctly - Use Formate yyyy-mm-dd");
        }
    }

}
