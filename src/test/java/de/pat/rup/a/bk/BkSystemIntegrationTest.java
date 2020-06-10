package de.pat.rup.a.bk;

import de.pat.rup.a.bk.models.ExchangeRateSummary;
import de.pat.rup.a.bk.models.TrendType;
import de.pat.rup.a.bk.repo.ExchangeRepoitory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
class BkSystemIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRepoitory exchangeRepoitory;

    @Test
    void checkIfResultIsWrittenToDB() throws Exception {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/2012-11-12/EUR/CAD")
                .contentType("application/json")).andExpect(status().isOk());
        ExchangeRateSummary result = exchangeRepoitory.findById(1).get();
        assertThat(expected.getExRate() == result.getExRate());
        assertThat(expected.getAverageExRate() == result.getAverageExRate());
        assertThat(expected.getTrendType() == result.getTrendType());
        assertThat(expected.getDate() == result.getDate());
    }

    @Test
    void checkIfResultIsCorrect() throws Exception {
        ExchangeRateSummary expected = new ExchangeRateSummary((float) 1.2725, (float) 1.2713801,
                TrendType.undefined, Date.valueOf("2012-11-12"));
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/2012-11-12/EUR/CAD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("exRate", is(1.2725)));
    }

    @Test
    void testExchangeRequest_expectErrorJson_wrongBaseCurreny() throws Exception {
        String expectedMessage = "You can not use ER as Base currency";
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/2012-11-12/ER/CAD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errors.[0].description", containsString(expectedMessage)));
    }

    @Test
    void testExchangeRequest_expectErrorJson_wrongTargetCurreny() throws Exception {
        String expectedMessage = "You can not use CD as Target currency";
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/2012-11-12/EUR/CD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errors.[0].description", containsString(expectedMessage)));
    }

    @Test
    void testExchangeRequest_expectErrorJson_timeBefore() throws Exception {
        String expectedMessage = "You are not allowed to use Dates before";
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/1999-11-12/EUR/CD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errors.[0].description", containsString(expectedMessage)));
    }

    @Test
    void testExchangeRequest_expectErrorJson_timeInFuture() throws Exception {
        String expectedMessage = "You are not allowed to use Dates";
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/2021-11-12/EUR/CD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errors.[0].description", containsString(expectedMessage)));
    }

    @Test
    void testExchangeRequest_expectErrorJson_timeMaleformed() throws Exception {
        String expectedMessage = "Date is not formed correctly";
        mockMvc.perform(get("http://localhost:8080/api/exchange-rate/199-11-12/EUR/CD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("errors.[0].description", containsString(expectedMessage)));
    }
}
