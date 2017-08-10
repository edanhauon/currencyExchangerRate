package com.shenkar.currency.control;

import com.shenkar.currency.model.ConcreteCurrencyDao;
import com.shenkar.currency.model.Currency;
import com.shenkar.currency.model.CurrencyDao;
import com.shenkar.currency.model.CurrencyXMLUpdater;
import com.shenkar.currency.view.CurrencyMainView;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyMainController {
    final static Logger logger = Logger.getLogger(CurrencyMainController.class);
    private CurrencyDao currencyDao;
    private CurrencyXMLUpdater currencyXMLUpdater;
    private CurrencyConverter currencyConverter;
    private CurrencyMainView mainView;

    private Thread mainViewThread;

    public CurrencyMainController() {
        currencyDao = new ConcreteCurrencyDao();
        currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao);
        mainView = new CurrencyMainView(this);
        currencyConverter = new CurrencyConverter();
    }

    public void run() {
        initThreads();

        System.out.println(convert("EGP", "GBP", 66));
        mainViewThread.start();
    }

    private double convert(String convertFrom, String convertTo, double amount) {
        return currencyConverter.convert(
                currencyDao.getCurrencyByCurrencyCode(convertFrom),
                currencyDao.getCurrencyByCurrencyCode(convertTo),
                amount
        );
    }

    private void initThreads() {
        mainViewThread = new Thread(mainView);
        currencyXMLUpdater.start();

        synchronized(currencyXMLUpdater) {
            try {
                //Waiting for xmlUpdater to finish populate all currencies
                currencyXMLUpdater.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(String currencyNameFrom, String currencyNameTo, double amount) {
        System.out.println(amount + " " + currencyNameFrom + " to " + currencyNameTo + " is = "
            + new DecimalFormat("#,###.##")
                .format(convert(currencyNameFrom, currencyNameTo, amount)));
        mainView.updateResult(convert(currencyNameFrom, currencyNameTo, amount));
    }

    public List<String> getCurrenciesNames() {
        //Get all currency code names
        return currencyDao.getCurrencies()
                .stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toList());
    }

    public List<Currency> getAllCurrencies() {
        return currencyDao.getCurrencies();
    }

    public String getLastUpdate() {
        return currencyXMLUpdater.getLastUpdate();
    }
}
