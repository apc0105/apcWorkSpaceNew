package com.news.model;

import java.io.Serializable;

public class Quantization implements Serializable {

    private String name;

    private String code;

    private float correlation;

    private float upsAndDowns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCorrelation() {
        return correlation;
    }

    public void setCorrelation(float correlation) {
        this.correlation = correlation;
    }

    public float getUpsAndDowns() {
        return upsAndDowns;
    }

    public void setUpsAndDowns(float upsAndDowns) {
        this.upsAndDowns = upsAndDowns;
    }
}
