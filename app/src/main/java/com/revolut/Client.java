package com.revolut;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static final String BASE_URL = "https://revolut.duckdns.org/";

    private static Client instance = new Client();
    private static Service service;

    public static Client getInstance() {
        return instance;
    }

    private Client() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())) // - что такое и зачем шедулер
                .build();

        service = retrofit.create(Service.class);
    }

    public Service getService() {
        return service;
    }
}