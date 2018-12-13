package com.tk.eventanalysis.model;

import java.io.Serializable;

public class EventAnalysis implements Serializable {

    private String company;

    private String code;

    private int star;

    private String[] gaugeNames;

    private String[] gaugeValues;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String[] getGaugeValues() {
        return gaugeValues;
    }

    public void setGaugeValues(String[] gaugeValues) {
        this.gaugeValues = gaugeValues;
    }

    public String[] getGaugeNames() {
        return gaugeNames;
    }

    public void setGaugeNames(String[] gaugeNames) {
        this.gaugeNames = gaugeNames;
    }
}
