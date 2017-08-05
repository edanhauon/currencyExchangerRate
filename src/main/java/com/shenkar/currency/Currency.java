package com.shenkar.currency;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "CURRENCY")
public class Currency {
    @JacksonXmlProperty(localName = "NAME")
    private String NAME;
    @JacksonXmlProperty(localName = "UNIT")
    private String UNIT;
    @JacksonXmlProperty(localName = "RATE")
    private String RATE;
    @JacksonXmlProperty(localName = "COUNTRY")
    private String COUNTRY;
    @JacksonXmlProperty(localName = "CHANGE")
    private String CHANGE;
    @JacksonXmlProperty(localName = "CURRENCYCODE")
    private String CURRENCYCODE;

    public Currency(String NAME, String UNIT, String RATE, String COUNTRY, String CHANGE, String CURRENCYCODE) {
        this.NAME = NAME;
        this.UNIT = UNIT;
        this.RATE = RATE;
        this.COUNTRY = COUNTRY;
        this.CHANGE = CHANGE;
        this.CURRENCYCODE = CURRENCYCODE;
    }

    public Currency() {
    }


    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getUNIT ()
    {
        return UNIT;
    }

    public void setUNIT (String UNIT)
    {
        this.UNIT = UNIT;
    }

    public String getRATE ()
    {
        return RATE;
    }

    public void setRATE (String RATE)
    {
        this.RATE = RATE;
    }

    public String getCOUNTRY ()
    {
        return COUNTRY;
    }

    public void setCOUNTRY (String COUNTRY)
    {
        this.COUNTRY = COUNTRY;
    }

    public String getCHANGE ()
    {
        return CHANGE;
    }

    public void setCHANGE (String CHANGE)
    {
        this.CHANGE = CHANGE;
    }

    public String getCURRENCYCODE ()
    {
        return CURRENCYCODE;
    }

    public void setCURRENCYCODE (String CURRENCYCODE)
    {
        this.CURRENCYCODE = CURRENCYCODE;
    }
}
