package de.pat.rup.a.bk.service;


import de.pat.rup.a.bk.models.Request;
import de.pat.rup.a.bk.util.BKException;

import java.util.List;

public interface IRemoteRequester {
    /**
     * Request ExchangeRate from RemoteApi
     *
     * @param request contianing query info
     * @return the exchangeRate as float
     * @throws BKException thrown when Currency is not valid
     */
    float requestRemoteExchangeRate(Request request) throws BKException;

    /**
     * Request 5 consecutive ExchangeRates (without sunday and saturda) from RemoteApi
     * and saves it a list, reverse ordered.
     *
     * @param request contianing query info
     * @return the exchangeRates as float in a List
     * @throws BKException thrown when Currency is not valid
     */
    List<Float> getFiveRatesReversedOrder(Request request) throws BKException;
}
