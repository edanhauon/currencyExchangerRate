package com.shenkar.currency;


import com.shenkar.currency.model.ConcreteCurrencyDao;
import com.shenkar.currency.model.CurrencyDao;

public class Application {
    public static void main(String[] args) throws Throwable{
        CurrencyDao currencyDao = new ConcreteCurrencyDao();
        CurrencyXMLUpdater currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao);

        Thread currencyUpdaterThread = new Thread(currencyXMLUpdater);
        currencyUpdaterThread.run();
    }


}
