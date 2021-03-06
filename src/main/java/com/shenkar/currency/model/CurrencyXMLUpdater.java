package com.shenkar.currency.model;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.shenkar.currency.CurrencyLogger;
import com.shenkar.currency.control.CurrencyMainController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyXMLUpdater extends Thread {
    private final static Logger logger = CurrencyLogger.init(CurrencyXMLUpdater.class);
    private static String URLToBOI = "http://www.boi.org.il/currency.xml";
    private static Charset defaultCharset = Charset.defaultCharset();
    private int timeToSleep;
    private ObjectReader objectReader;
    private File xmlFile;
    private CurrencyDao currencyDaoObserver;
    private CurrencyMainController currencyMainControllerObserver;
    private boolean keepUpdating;
    private String lastUpdate;

    public CurrencyXMLUpdater(CurrencyDao currencyDaoObserver, CurrencyMainController currencyMainControllerObserver) {
        logger.warn("Instantiating CurrencyXMLUpdater");
        objectReader = new XmlMapper().readerFor(Currency[].class); // This is for parsing - XML -> Currencies

        try {
            xmlFile = new File("rawXML.xml"); //Data File

            //This is to get a diff in the first invokeConversionRequest
            FileUtils.write(xmlFile, "", defaultCharset);
        } catch (IOException e) {
            logger.warn("An error occurred while writing to file");
            e.printStackTrace();
        }

        timeToSleep = 40*1000; //In ms
        this.currencyDaoObserver = currencyDaoObserver;
        this.currencyMainControllerObserver = currencyMainControllerObserver;
        keepUpdating = true;
    }

    private List<Currency> parseXMLToCurrencies(String rawXMLString) throws Exception {
        Currency[] currencies;
        try {
            logger.info("Parsing Currencies from XML");
            currencies = objectReader.readValue(rawXMLString);

            //Otherwise, need to write changes into file
            FileUtils.write(xmlFile, rawXMLString, defaultCharset);

        } catch (JsonMappingException e) {
            logger.warn("Problem with XML parsing (could be internet issues) - reading from last known XML");
            currencies = objectReader.readValue(FileUtils.readFileToString(xmlFile, defaultCharset));
        }

        return Arrays.asList(currencies);
    }

    private String checkForUpdate() throws Exception {
        InputStream xmlInputStream = new URL(URLToBOI).openStream();
        String newRawXMLString = IOUtils.toString(xmlInputStream, defaultCharset);

        //This part is to help ObjectReader - I don't want him to think "LAST_UPDATE" is a currency
        String newXMLString = newRawXMLString.replaceAll("<LAST_UPDATE>.*</LAST_UPDATE>\n","");

        //In order to compare to the new XML - should have "LAST_UPDATE" already removed
        String oldXMLString = FileUtils.readFileToString(xmlFile, defaultCharset);

        //"null" indicates there hasn't been any changes
        if (oldXMLString.equals(newXMLString))
            return null;

        logger.info("Found diff from current know currencies (happens in initial start as well)");
        Matcher matcher = Pattern
                .compile("<LAST_UPDATE>(.*)</LAST_UPDATE>")
                .matcher(newRawXMLString);
        if (matcher.find())
            lastUpdate = matcher.group(1);
        return newXMLString;
    }

    @Override
    public void run() {
        try {
            while (keepUpdating) { //So it can be stopped by a setter
                logger.info("Checking for updates");
                String newXMLString = checkForUpdate();
                if (newXMLString != null) {
                    //Giving the Dao the new list of currencies
                    currencyDaoObserver.update(parseXMLToCurrencies(newXMLString));
                    synchronized (this) {
                        //This is to notify the application that I'm done with fetching currencies
                        // and that they can be accessed
                        this.notify();
                    }
                } else {
                    logger.info("No updates for now..");
                    currencyMainControllerObserver.updateWithLatestChange();
                }
                //Creating gaps between requests
                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    logger.debug("Sleep interrupted");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public int getTimeToSleep() {
        return timeToSleep;
    }

    public void setTimeToSleep(int timeToSleep) {
        this.timeToSleep = timeToSleep;
    }

    public boolean isKeepUpdating() {
        return keepUpdating;
    }

    public void setKeepUpdating(boolean keepUpdating) {
        this.keepUpdating = keepUpdating;
    }
}
