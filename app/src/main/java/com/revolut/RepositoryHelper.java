package com.revolut;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class RepositoryHelper {
    public static ArrayList<Currency> convertCurrencyListData(CurrencyResponse response, Currency baseCurrency) {
        Set<Map.Entry<String, Float>> entrySet = response.rates.entrySet();
        ArrayList<Currency> currencyList = new ArrayList<>();
        for (Map.Entry<String, Float> entry : entrySet) {
            currencyList.add(new Currency(entry.getKey(), entry.getValue() * baseCurrency.getValue()));
        }
        return currencyList;
    }
}
