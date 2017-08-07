package com.shenkar.currency;

import com.shenkar.currency.control.CurrencyConverter;
import com.shenkar.currency.model.ConcreteCurrencyDao;
import com.shenkar.currency.model.CurrencyDao;
import com.shenkar.currency.model.CurrencyXMLUpdater;
import com.shenkar.currency.view.CurrencyMainView;


public class Application {
    public static void main(String[] args) throws Throwable{
        CurrencyDao currencyDao = new ConcreteCurrencyDao();
        CurrencyXMLUpdater currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao);

        Thread currencyUpdaterThread = new Thread(currencyXMLUpdater);
        currencyUpdaterThread.start();

        /*Thread.sleep(3000);
        System.out.println(CurrencyConverter.convert(
                currencyDao.getCurrencyByCurrencyCode("EGP"),
                currencyDao.getCurrencyByCurrencyCode("GBP"),
                66
        ));
        currencyXMLUpdater.setKeepUpdating(false);*/


        CurrencyMainView mainView = new CurrencyMainView();
        mainView.run();
    }


}
