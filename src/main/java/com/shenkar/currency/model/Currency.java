package com.shenkar.currency.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "CURRENCY")
public class Currency {
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    @JacksonXmlProperty(localName = "UNIT")
    private int unit;
    @JacksonXmlProperty(localName = "RATE")
    private double rate;
    @JacksonXmlProperty(localName = "COUNTRY")
    private String country;
    @JacksonXmlProperty(localName = "CHANGE")
    private double change;
    @JacksonXmlProperty(localName = "CURRENCYCODE")
    private String currencyCode;

    public Currency(String name, int unit, long rate, String country, long change, String currencyCode) {
        this.name = name;
        this.unit = unit;
        this.rate = rate;
        this.country = country;
        this.change = change;
        this.currencyCode = currencyCode;
    }

    public Currency() {
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getUnit()
    {
        return unit;
    }

    public void setUnit(int unit)
    {
        this.unit = unit;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public double getChange()
    {
        return change;
    }

    public void setChange(double change)
    {
        this.change = change;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }
}
