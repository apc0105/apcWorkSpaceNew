package com.tk.eventanalysis.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalysisDetail implements Serializable {

    private Long date;

    private String title;

    private String url;

    private float coeffi;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getCoeffi() {
        return coeffi;
    }

    public void setCoeffi(float coeffi) {
        this.coeffi = coeffi;
    }

    public String getDetailDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = new Date(this.date);

        return sdf.format(date1);
    }
}
