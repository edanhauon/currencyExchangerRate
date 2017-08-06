package com.shenkar.currency.model;

//import static com.google.common.collect.MoreCollectors.onlyElement;
import java.util.List;
import java.util.Map;

public final class ConcreteCurrencyDao implements CurrencyDao {
    private List<Currency> currencies;

    public ConcreteCurrencyDao() {
    }

    public ConcreteCurrencyDao(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Currency getCurrencyByName(String name) {
        return currencies
                .stream()
                .filter((currency) -> currency.getName().equals(name)).findAny().get();
                //.collect(onlyElement());
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
