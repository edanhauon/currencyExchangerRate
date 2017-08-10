package com.shenkar.currency.control;

import com.shenkar.currency.CurrencyLogger;
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
    private final static Logger logger = CurrencyLogger.init(CurrencyMainController.class);
    private CurrencyDao currencyDao;
    private CurrencyXMLUpdater currencyXMLUpdater;
    private CurrencyConverter currencyConverter;
    private CurrencyMainView mainView;

    private Thread mainViewThread;

    public CurrencyMainController() {
        logger.info("Instantiating CurrencyMainController");
        currencyDao = new ConcreteCurrencyDao();
        currencyXMLUpdater = new CurrencyXMLUpdater(currencyDao, this);
        mainView = new CurrencyMainView(this);
        currencyConverter = new CurrencyConverter();
    }

    public void run() {
        logger.info("Initializing threads");
        initThreads();
    }

    private double convert(String convertFrom, String convertTo, double amount) {
        logger.info("Conversion triggered!");
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
                logger.warn("An error occurred while waiting for tables");
                e.printStackTrace();
            }
        }
        mainViewThread.start();
    }

    public void invokeConversionRequest(String currencyNameFrom, String currencyNameTo, double amount) {
        logger.debug("Update triggered on Controller");
        double total = convert(currencyNameFrom, currencyNameTo, amount);
        logger.debug("Converted " + amount + " (" + currencyNameFrom + ") to (" + currencyNameTo + ") = "
                + new DecimalFormat("#,###.##").format(total));
        mainView.updateResult(total);
    }

    public void invokeRefreshRequest() {
        logger.debug("Refresh request triggered");
        currencyXMLUpdater.interrupt();
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

    public void updateWithLatestChange() {
        mainView.updateLastUpdate();
    }
}
