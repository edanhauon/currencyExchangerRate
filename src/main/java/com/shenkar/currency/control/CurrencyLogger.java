package com.shenkar.currency.control;

import org.apache.log4j.*;

import java.io.IOException;

public class CurrencyLogger {
    private static FileAppender fileAppender = null;
    private static ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"));

    public static Logger init(Class clazz) {
        Logger LOGGER = Logger.getLogger(clazz);
        LOGGER.setAdditivity(false);
        BasicConfigurator.configure();
        if (fileAppender == null) {
            try {
                fileAppender = new FileAppender(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"),
                        "out.log");
            } catch (IOException e) {
                System.out.println("Something went wrong with your logger file");
                e.printStackTrace();
            }
        }
        LOGGER.addAppender(fileAppender);
        LOGGER.addAppender(consoleAppender);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);

        return LOGGER;
    }
}

