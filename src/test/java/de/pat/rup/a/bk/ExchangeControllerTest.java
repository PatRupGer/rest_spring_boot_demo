package de.pat.rup.a.bk;

import de.pat.rup.a.bk.controller.ExchangeRateController;
import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.models.TrendType;
import de.pat.rup.a.bk.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ExchangeService exchangeService;

    @Test
    public void getExchangeRateSummaryTest() throws Exception {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        given(exchangeService.getExchangeRate(any(Request.class))).willReturn(expected);
        mvc.perform(get("/api/exchange-rate/2012-11-12/EUR/CAD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("trendType", is("undefined")))
                .andExpect(jsonPath("exRate", is(1.2725)));
    }

    @Test
    public void getExchangeRateHistoryDailyTest() throws Exception {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        List<ExchangeRateSummary> results = new ArrayList<>();
        results.add(expected);
        given(exchangeService.getExchangeRateHistoryDaily(any(Date.class))).willReturn(results);
        mvc.perform(get("/api/exchange-rate//history/daily/2012/11/12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].trendType", is("undefined")))
                .andExpect(jsonPath("$[0].exRate", is(1.2725)));
    }

    @Test
    public void getExchangeRateHistoryDailyTest_malformedYear() throws Exception {
        mvc.perform(get("/api/exchange-rate//history/daily/201/09/12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].description", containsString("not formed correctly")));
    }
    @Test
    public void getExchangeRateHistoryDailyTest_malformedMonth() throws Exception {
        mvc.perform(get("/api/exchange-rate//history/daily/2012/9/12")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].description", containsString("not formed correctly")));
    }

    @Test
    public void getExchangeRateHistoryDailyTest_malformedDay() throws Exception {
        mvc.perform(get("/api/exchange-rate//history/daily/2012/09/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].description", containsString("not formed correctly")));
    }

    @Test
    public void getExchangeRateHistoryMontlyTest() throws Exception {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        List<ExchangeRateSummary> results = new ArrayList<>();
        results.add(expected);
        given(exchangeService.getExchangeRateHistoryMonthly(any(Date.class))).willReturn(results);
        mvc.perform(get("/api/exchange-rate/history/monthly/2012/11")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].trendType", is("undefined")))
                .andExpect(jsonPath("$[0].exRate", is(1.2725)));
    }
    @Test
    public void getExchangeRateHistoryMonthlyTest_malformedYear() throws Exception {
        mvc.perform(get("/api/exchange-rate//history/monthly/201/09")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].description", containsString("not formed correctly")));
    }
    @Test
    public void getExchangeRateHistoryMontlyTest_malformedMonth() throws Exception {
        mvc.perform(get("/api/exchange-rate//history/monthly/2012/9")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].description", containsString("not formed correctly")));
    }

}
