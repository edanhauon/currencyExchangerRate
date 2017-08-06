package com.shenkar.currency;


public class Application {
    public static void main(String[] args) throws Throwable{
        CurrencyDao currencyDao = new ConcreteCurrencyDao();
        CurrencyXMLUpdater currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao);

        Thread currencyUpdaterThread = new Thread(currencyXMLUpdater);
        currencyUpdaterThread.run();
    }


}
