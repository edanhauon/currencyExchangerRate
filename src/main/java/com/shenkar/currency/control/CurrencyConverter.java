package com.shenkar.currency.control;

import com.shenkar.currency.model.Currency;


public class CurrencyConverter {
    public double convert(Currency convertFrom, Currency convertTo, double amount) {
        double convertFromRateInOneUnit = convertFrom.getRate()/convertFrom.getUnit();
        double convertToRateInOneUnit = convertTo.getRate()/convertTo.getUnit();

        return (amount*convertFromRateInOneUnit)/convertToRateInOneUnit;
    }
}
