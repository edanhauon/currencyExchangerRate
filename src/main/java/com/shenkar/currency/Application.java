package com.shenkar.currency;

import com.shenkar.currency.control.CurrencyMainController;
import org.apache.log4j.Logger;


public class Application {
    private final static Logger logger = CurrencyLogger.init(Application.class);

    public static void main(String[] args) throws Throwable{
        logger.info("Starting application");
        CurrencyMainController currencyMainController = new CurrencyMainController();
        currencyMainController.run();
        logger.info("Application closed");

    }
}
