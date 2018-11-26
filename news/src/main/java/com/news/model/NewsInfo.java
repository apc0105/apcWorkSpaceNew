package com.news.model;

import java.io.Serializable;
import java.util.Map;

public class NewsInfo implements Serializable {

    private String name;

    private String code;

    private  double score;

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}
