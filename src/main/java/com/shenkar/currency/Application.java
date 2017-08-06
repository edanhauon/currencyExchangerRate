package com.shenkar.currency;


import com.shenkar.currency.control.CurrencyConverter;
import com.shenkar.currency.model.ConcreteCurrencyDao;
import com.shenkar.currency.model.CurrencyDao;
import com.shenkar.currency.model.CurrencyXMLUpdater;

public class Application {
    public static void main(String[] args) throws Throwable{
        CurrencyDao currencyDao = new ConcreteCurrencyDao();
        CurrencyXMLUpdater currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao);

        Thread currencyUpdaterThread = new Thread(currencyXMLUpdater);
        currencyUpdaterThread.start();

        Thread.sleep(4000);
        System.out.println(CurrencyConverter.convert(
                currencyDao.getCurrencyByName("Yen"),
                currencyDao.getCurrencyByName("Pound"),
                88
        ));
        currencyXMLUpdater.setKeepUpdating(false);
    }


}
