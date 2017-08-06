package com.shenkar.currency;


import java.util.List;

public final class ConcreteCurrencyDataHolder implements CurrencyDataHolder {
    private List<Currency> currencies;

    public ConcreteCurrencyDataHolder() {
    }

    public ConcreteCurrencyDataHolder(List<Currency> currencies) {
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
