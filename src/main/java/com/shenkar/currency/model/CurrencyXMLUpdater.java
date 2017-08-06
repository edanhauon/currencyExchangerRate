package com.shenkar.currency.model;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class CurrencyXMLUpdater implements Runnable {
    private static String URLToBOI = "http://www.boi.org.il/currency.xml";
    private static Charset defaultCharset = Charset.defaultCharset();
    private int timeToSleep;
    private ObjectReader objectReader;
    private File xmlFile;
    private CurrencyDao currencyDaoObserver;
    private boolean keepUpdating;

    public CurrencyXMLUpdater(CurrencyDao currencyDaoObserver) throws Exception{
        objectReader = new XmlMapper().readerFor(Currency[].class);

        xmlFile = new File("~/rawXML");

        //This is to get a diff in the first update
        FileUtils.write(xmlFile, "", defaultCharset);

        timeToSleep = 5*1000; //In ms
        this.currencyDaoObserver = currencyDaoObserver;
        keepUpdating = true;
    }

    private List<Currency> parseXMLToCurrencies(String rawXMLString) throws Exception {
        Currency[] currencies = objectReader.readValue(rawXMLString);
        FileUtils.write(xmlFile, rawXMLString, defaultCharset);

        return Arrays.asList(currencies);
    }

    private String checkForUpdate() throws Exception {
        InputStream xmlInputStream = new URL(URLToBOI).openStream();
        String newXMLString = IOUtils.toString(xmlInputStream, defaultCharset).replaceAll("<LAST_UPDATE>.*</LAST_UPDATE>\n","");

        String oldXMLString = FileUtils.readFileToString(xmlFile, defaultCharset);

        //"null" indicates there hasn't been any changes
        if (oldXMLString.equals(newXMLString))
            return null;

        System.out.println("There is a diff");
        //Otherwise, need to write changes into file
        FileUtils.write(xmlFile, newXMLString, defaultCharset);
        return newXMLString;
    }

    public void run() {
        try {
            while (keepUpdating) { //So it can be stopped
                System.out.println("Checking for update");
                String newXMLString = checkForUpdate();
                if (newXMLString != null)
                    currencyDaoObserver.update(
                            parseXMLToCurrencies(newXMLString));
                System.out.println("Update checked... going to sleep");
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
