package com.shenkar.currency.model;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.shenkar.currency.control.CurrencyLogger;
import com.shenkar.currency.view.CurrencyMainView;
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
    final static Logger logger = CurrencyLogger.init(CurrencyXMLUpdater.class);
    private static String URLToBOI = "http://www.boi.org.il/currency.xml";
    private static Charset defaultCharset = Charset.defaultCharset();
    private int timeToSleep;
    private ObjectReader objectReader;
    private File xmlFile;
    private CurrencyDao currencyDaoObserver;
    private boolean keepUpdating;

    public String getLastUpdate() {
        return lastUpdate;
    }

    private String lastUpdate;

    public CurrencyXMLUpdater(CurrencyDao currencyDaoObserver) {
        objectReader = new XmlMapper().readerFor(Currency[].class); // This is for parsing - XML -> Currencies

        try {
            xmlFile = new File("rawXML.xml"); //Data File

            //This is to get a diff in the first update
            FileUtils.write(xmlFile, "", defaultCharset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        timeToSleep = 40*1000; //In ms
        this.currencyDaoObserver = currencyDaoObserver;
        keepUpdating = true;
    }

    private List<Currency> parseXMLToCurrencies(String rawXMLString) throws Exception {
        Currency[] currencies;
        try {
            currencies = objectReader.readValue(rawXMLString);

            //Otherwise, need to write changes into file
            FileUtils.write(xmlFile, rawXMLString, defaultCharset);

        } catch (JsonMappingException e) {
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
                String newXMLString = checkForUpdate();
                if (newXMLString != null) {
                    //Giving the Dao the new list of currencies
                    currencyDaoObserver.update(parseXMLToCurrencies(newXMLString));
                    synchronized (this) {
                        //This is to notify the application that I'm done with fetching currencies
                        // and that they can be accessed
                        this.notify();
                    }
                }
                //Creating gaps between requests
                Thread.sleep(timeToSleep);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
