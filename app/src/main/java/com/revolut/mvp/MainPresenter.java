package com.revolut.mvp;

import com.revolut.Currency;
import com.revolut.CurrencyResponse;
import com.revolut.RepositoryHelper;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    private static final String BASE_CURRENCY_NAME = "EUR";
    private static final Float BASE_CURRENCY_VALUE = 1F;

    private MainContract.View view;
    private MainContract.Repository repository;

    private List<Currency> currencyList;
    private Currency baseCurrency;

    MainPresenter(MainContract.View view) {
        this.view = view;
        this.repository = new MainRepository(this);
        this.baseCurrency = new Currency(BASE_CURRENCY_NAME, BASE_CURRENCY_VALUE);
    }

    @Override
    public void onResume() {
        repository.getLast();
        if (view != null) {
            view.showEmptyData();
            view.showBaseCurrency(baseCurrency);
        }
    }


    @Override
    public void onDestroy() {
        repository.unSubscribe();
    }

    @Override
    public void onBaseCurrencySelected(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
        if (view != null) {
            view.showBaseCurrency(baseCurrency);
        }
    }

    @Override
    public void onBaseCurrencyValueChanged(String baseCurrencyValue) {
        if (baseCurrencyValue.isEmpty()) {
            this.baseCurrency.setValue(0F);
            return;
        }
        this.baseCurrency.setValue(Float.parseFloat(baseCurrencyValue));
    }


    @Override
    public void onDataLoadedSuccess(CurrencyResponse currencyResponse) {
        this.currencyList = RepositoryHelper.convertCurrencyListData(currencyResponse, baseCurrency);
        if (view != null) {
            view.showData(currencyList);
        }
    }

    @Override
    public void onDataLoadedError() {
        if (view != null) {
            view.showEmptyData();
        }
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }
}