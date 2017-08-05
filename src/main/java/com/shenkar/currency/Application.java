package com.shenkar.currency;


public class Application {
    public static void main(String[] args) throws Throwable{
        CurrencyDataHolder currencyDataHolder = new CurrencyDataHolder();
        CurrencyXMLUpdater currencyXMLUpdater = new CurrencyXMLUpdater(currencyDataHolder);

        Thread currencyUpdaterThread = new Thread(currencyXMLUpdater);
        currencyUpdaterThread.run();
        System.out.println("Added nothing");
    }


}
