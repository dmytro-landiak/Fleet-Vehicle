package com.landim.entity;

import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cityID;
    private String cityName;


    public City(String cityName) {
        this.cityName = cityName;
    }

    public City() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getCityID() {
        return cityID;
    }

    public void setCityID(Integer cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
