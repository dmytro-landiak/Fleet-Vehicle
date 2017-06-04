package com.landim.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "TRIP")
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;

    private long tripID;
    private String day;
    private String time;
    private Integer passengCount;
    private Route route;

    public Trip(String day, String time, Integer passengCount) {
        this.day = day;
        this.time = time;
        this.passengCount = passengCount;
    }

    public Trip(String day, String time, Integer passengCount, Route route) {
        this.day = day;
        this.time = time;
        this.passengCount = passengCount;
        this.route = route;
    }

    public Trip() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "TRIP_ID")
    @GeneratedValue
    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
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

    @ManyToOne
    public Route getRoute() { return route; }

    public void setRoute(Route route) {
        this.route = route;
    }
}

