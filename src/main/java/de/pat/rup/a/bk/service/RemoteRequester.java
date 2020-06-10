package de.pat.rup.a.bk.service;

import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.util.BKException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.pat.rup.a.bk.util.FINALS.BASE_URI;
import static de.pat.rup.a.bk.util.FINALS.FIRSTDATE;

@Service
public class RemoteRequester implements IRemoteRequester {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
    Logger logger = Logger.getLogger(RemoteRequester.class.getName());

    /**
     * Request Exchange rates from "https://api.exchangeratesapi.io/
     *
     * @param request user Request
     * @return the exchange rate
     * @throws BKException if Base or Target currency are unsupported, or if Date is not valid
     */
    @Override
    public float requestRemoteExchangeRate(Request request) throws BKException {
        String restUrl = BASE_URI + request.getDate().toString() + "?base=" + request.getBaseCurrency()
                + "&symbols=" + request.getTargetCurrency();
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(restUrl))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String result = response.body().split(":")[2].split("}")[0];
                return Float.parseFloat(result);
            } else if (response.body().contains("Base") && response.body().contains("not supported")) {
                throw new BKException("You can not use " + request.getBaseCurrency() + " as Base currency");
            } else if (response.body().contains("Symbols") && response.body().contains("invalid for date")) {
                throw new BKException("You can not use " + request.getTargetCurrency() + " as Target currency");
            }
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Request went Wrong " + response.body());
        }
        return 0;
    }

    /**
     * Request Exchangerate from befor 5 days of original request without sunday and saturday,
     *
     * @param request original request
     * @return a list of rates from 5 days in reversOrder
     */
    public List<Float> getFiveRatesReversedOrder(Request request) throws BKException {
        int daysCounter = 1;
        int maxCount = 5; //without weekend
        List<Float> rates = new ArrayList<>();
        // request exchange Rates 5 Days before Request Day without Sunday and Saturday
        // and save in list
        while (daysCounter < maxCount) {
            LocalDate requestDate = LocalDate.parse(request.getDate().toString());
            LocalDate tempDate = requestDate.minusDays(daysCounter);
            if (tempDate.isBefore(FIRSTDATE)) {
                break;
            }
            Calendar tempCalendar = Calendar.getInstance();
            tempCalendar.set(tempDate.getYear(), tempDate.getMonth().getValue(), tempDate.getDayOfMonth());
            if (tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    tempCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                maxCount++;
            } else {
                rates.add(requestRemoteExchangeRate(new Request(Date.valueOf(tempDate.toString()),
                        request.getBaseCurrency(), request.getTargetCurrency())));
            }
            daysCounter++;
        }
        return rates;
    }


}
