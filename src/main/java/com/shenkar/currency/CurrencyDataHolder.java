package com.shenkar.currency;


import java.util.List;

public final class CurrencyDataHolder {
    private List<Currency> currencies;
    private String lastUpdate;
    public CurrencyDataHolder() {
    }

    public CurrencyDataHolder(List<Currency> currencies) {
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
