package de.pat.rup.a.bk;

import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.service.RemoteRequester;
import de.pat.rup.a.bk.util.BKException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = RemoteRequester.class)
public class RemoteRequesterTest {
    @Autowired
    RemoteRequester remoteRequester;

    @Test
    public void validRequest() {
        Request request = new Request(Date.valueOf("2012-12-12"), "USD", "EUR");
        float result;
        try {
            result = remoteRequester.requestRemoteExchangeRate(request);
            assertThat(result == 0.7668711656);
        } catch (BKException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wrongBaseCurrency_Exception() {
        Request request = new Request(Date.valueOf("2012-12-12"), "US", "EUR");
        Exception exception = assertThrows(BKException.class, () -> {
            remoteRequester.requestRemoteExchangeRate(request);
        });
        String expectedMessage = "You can not use US as Base currency";
        assertThat(expectedMessage.equals(exception.getMessage()));
    }

    @Test
    public void wrongTargetCurrency_Exception() {
        Request request = new Request(Date.valueOf("2012-12-12"), "USD", "ER");
        Exception exception = assertThrows(BKException.class, () -> {
            remoteRequester.requestRemoteExchangeRate(request);
        });
        String expectedMessage = "You can not use ER as Target currency";
        assertThat(expectedMessage.equals(exception.getMessage()));
    }

    @Test
    public void testFiveRequest_borderCase() throws BKException {
        Request request = new Request(Date.valueOf("2000-01-03"), "USD", "EUR");
        List<Float> results = remoteRequester.getFiveRatesReversedOrder(request);
        assertThat(results.size()==3);
    }
    @Test
    public void testFiveRequest_valid() throws BKException {
        Request request = new Request(Date.valueOf("2008-01-03"), "USD", "EUR");
        List<Float> results = remoteRequester.getFiveRatesReversedOrder(request);
        assertThat(results.size()==5);
    }
}
