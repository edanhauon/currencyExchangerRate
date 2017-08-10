package com.shenkar.currency.model;

import java.util.List;

public interface CurrencyDao { //Currency Data Access Object Layer
    Currency getCurrencyByCurrencyCode(String currencyCode);

    List<Currency> getCurrencies();

    void setCurrencies(List<Currency> currencies);

    void update(List<Currency> currencies);
}
