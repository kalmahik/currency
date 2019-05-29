package com.revolut;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    @GET("latest")
    Observable<CurrencyResponse> listCurrencies(@Query("base") String currency);
}
