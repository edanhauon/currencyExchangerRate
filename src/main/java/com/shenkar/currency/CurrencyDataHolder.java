package com.shenkar.currency;

import java.util.List;

/**
 * Created by edanhauon on 06/08/2017.
 */
public interface CurrencyDataHolder {
    List<Currency> getCurrencies();
    void setCurrencies(List<Currency> currencies);
    void update(List<Currency> currencies);
}
