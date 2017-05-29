package com.landim.entity;

import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer tripID;
    private String day;
    private String time;
    private Integer passengCount;


    public Trip(String day, String time, Integer passengCount) {
        this.day = day;
        this.time = time;
        this.passengCount = passengCount;
    }

    public Trip() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTripID() {
        return tripID;
    }

    public void setTripID(Integer tripID) {
        this.tripID = tripID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPassengCount() {
        return passengCount;
    }

    public void setPassengCount(Integer passengCount) {
        this.passengCount = passengCount;
    }
}

