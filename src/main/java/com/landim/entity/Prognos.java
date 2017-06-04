package com.landim.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "PROGNOS")
public class Prognos implements Serializable {
    private static final long serialVersionUID = 1L;

    private long prognosID;
    private String day;
    private String time;
    private Integer prognosCount;
    private Route route;

    public Prognos(String day, String time, Integer prognosCount) {
        this.day = day;
        this.time = time;
        this.prognosCount = prognosCount;
    }

    public Prognos(String day, String time, Integer prognosCount, Route route) {
        this.day = day;
        this.time = time;
        this.prognosCount = prognosCount;
        this.route = route;
    }

    public Prognos() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "PROGNOS_ID")
    @GeneratedValue
    public long getPrognosID() {
        return prognosID;
    }

    public void setPrognosID(long prognosID) {
        this.prognosID = prognosID;
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

    public Integer getPrognosCount() {
        return prognosCount;
    }

    public void setPrognosCount(Integer prognosCount) {
        this.prognosCount = prognosCount;
    }

    @ManyToOne
    @JoinColumn(name = "route_ROUTE_ID")
    public Route getRoute() { return route; }

    public void setRoute(Route route) {
        this.route = route;
    }
}

