package de.pat.rup.a.bk;


import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.models.TrendType;
import de.pat.rup.a.bk.repo.ExchangeRepoitory;
import de.pat.rup.a.bk.service.CalculatorService;
import de.pat.rup.a.bk.service.ExchangeService;
import de.pat.rup.a.bk.service.IExchangeService;
import de.pat.rup.a.bk.service.IRemoteRequester;
import de.pat.rup.a.bk.util.BKException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = ExchangeService.class)
public class ExchangeServiceTest {

    @Autowired
    private IExchangeService exchangeService;
    @MockBean
    private ExchangeRepoitory exchangeRepoitory;
    @MockBean
    private CalculatorService calculatorService;
    @MockBean
    private IRemoteRequester remoteRequester;

    @BeforeEach
    public void setUp() throws BKException {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.descending, Date.valueOf("2012-12-12"));
        List<ExchangeRateSummary> results = new ArrayList<>();
        results.add(expected);

        Mockito.when(exchangeRepoitory.save(any())).thenReturn(expected);
        Mockito.when(exchangeRepoitory.daily(any(Date.class)))
                .thenReturn(results);
        Mockito.when(exchangeRepoitory.monthly(any(Date.class), any(Date.class)))
                .thenReturn(results);
        Mockito.when(calculatorService.evaluateTrend(any()))
                .thenReturn(expected.getTrendType());
        Mockito.when(calculatorService.isConstant(any()))
                .thenReturn(false);
        Mockito.when(calculatorService.reversedListIsAscending(any()))
                .thenReturn(false);
        Mockito.when(calculatorService.reversedListIsDescending(any()))
                .thenReturn(false);
        Mockito.when(calculatorService.calcAverage(any()))
                .thenReturn(expected.getAverageExRate());

        List<Float> exchangeRates = List.of(1f, 2f, 3f, 4f, 5f);
        Mockito.when(remoteRequester.getFiveRatesReversedOrder(any()))
                .thenReturn(exchangeRates);
        Mockito.when(remoteRequester.requestRemoteExchangeRate(any()))
                .thenReturn((float) 0.001);
    }

    @Test
    public void testGetExchangeRate() throws BKException {
        Request any = new Request(Date.valueOf("2012-12-12"), "USD", "EUR");
        ExchangeRateSummary result = exchangeService.getExchangeRate(any);

        assertThat(result.getTrendType()).isEqualTo(TrendType.descending);
        assertThat(result.getDate()).isEqualTo(Date.valueOf("2012-12-12"));
        assertThat(result.getExRate()).isEqualTo(1.2725f);
    }

    @Test
    public void testGetHistoryDaily() throws BKException {
        Date test = Date.valueOf("2012-12-12");
        List<ExchangeRateSummary> result = exchangeService.getExchangeRateHistoryDaily(test);

        assertThat(result.size() == 1);
        assertThat(result.get(0).getDate()).isEqualTo(Date.valueOf("2012-12-12"));
        assertThat(result.get(0).getExRate()).isEqualTo(1.2725f);
    }

    @Test
    public void testGetHistoryMonthly() throws BKException {
        Date test = Date.valueOf("2012-12-12");
        List<ExchangeRateSummary> result = exchangeService.getExchangeRateHistoryMonthly(test);

        assertThat(result.size() == 1);
        assertThat(result.get(0).getDate()).isEqualTo(Date.valueOf("2012-12-12"));
        assertThat(result.get(0).getExRate()).isEqualTo(1.2725f);
    }

    @TestConfiguration
    static class ExchangeServiceTestContextConfiguration {
        @Bean
        public ExchangeService exchangeService() {
            return new ExchangeService();
        }
    }
}
