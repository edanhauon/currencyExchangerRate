package com.shenkar.currency.model;

import java.util.List;

public final class ConcreteCurrencyDao implements CurrencyDao {
    private List<Currency> currencies;

    public ConcreteCurrencyDao() {
    }

    public ConcreteCurrencyDao(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Currency getCurrencyByCurrencyCode(String currencyCode) {
        return currencies
                .stream()
                .filter((currency) -> currency.getCurrencyCode().equals(currencyCode))
                .findAny() //Should get only one - if I got more then something went wrong
                .get();
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
