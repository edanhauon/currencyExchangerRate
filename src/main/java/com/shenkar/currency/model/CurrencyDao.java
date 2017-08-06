package com.shenkar.currency.model;

import java.util.List;

/**
 * Created by edanhauon on 06/08/2017.
 */
public interface CurrencyDao {
    Currency getCurrencyByName(String name);

    List<Currency> getCurrencies();

    void setCurrencies(List<Currency> currencies);

    void update(List<Currency> currencies);
}
