package com.shenkar.currency.model;


import java.util.List;

public final class ConcreteCurrencyDao implements CurrencyDao {
    private List<Currency> currencies;

    public ConcreteCurrencyDao() {
    }

    public ConcreteCurrencyDao(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public void update(List<Currency> currencies) {
        setCurrencies(currencies);
    }
}
