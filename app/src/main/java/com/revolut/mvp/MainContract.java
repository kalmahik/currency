package com.revolut.mvp;

import com.revolut.Currency;
import com.revolut.CurrencyResponse;

import java.util.List;

public interface MainContract {
    interface View {
        void showData(List<Currency> currencyList);

        void showEmptyData();

        void showBaseCurrency(Currency baseCurrency);

    }

    interface Presenter {
        void onResume();

        void onDestroy();

        void onDataLoadedSuccess(CurrencyResponse currencyResponse);

        void onDataLoadedError();

        void onBaseCurrencySelected(Currency baseCurrency);

        void onBaseCurrencyValueChanged(String baseCurrencyValue);

    }

    interface Repository {
        void getLast();

        void unSubscribe();
    }
}