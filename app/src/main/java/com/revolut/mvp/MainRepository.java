package com.revolut.mvp;

import com.revolut.Client;
import com.revolut.CurrencyResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainRepository implements MainContract.Repository {
    private static final int DELAY_IN_SECONDS = 1;
    private MainPresenter presenter;
    private Observer<CurrencyResponse> observer = new Observer<CurrencyResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(CurrencyResponse currencyResponse) {
            presenter.onDataLoadedSuccess(currencyResponse);
        }

        @Override
        public void onError(Throwable e) {
            presenter.onDataLoadedError();
        }

        @Override
        public void onComplete() {
        }
    };
    private Disposable disposable;


    public MainRepository(MainPresenter mainPresenter) {
        this.presenter = mainPresenter;
    }

    @Override
    public void getLast() {
        Observable.just(1)
                .repeatWhen(completed -> completed.delay(DELAY_IN_SECONDS, TimeUnit.SECONDS))
                .flatMap(v -> Client
                        .getInstance()
                        .getService()
                        .listCurrencies(presenter.getBaseCurrency().getName())
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void unSubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}