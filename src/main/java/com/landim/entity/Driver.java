package com.landim.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "BUS")
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private long driverID;
    private String driverName;
    private String phone;
    private City city;
    private Bus bus;
    private Route route;

    public Driver(String driverName, String phone) {
        this.driverName = driverName;
        this.phone = phone;
    }

    public Driver(String driverName, String phone, City city, Bus bus, Route route) {
        this.driverName = driverName;
        this.phone = phone;
        this.city = city;
        this.bus = bus;
        this.route = route;
    }

    public Driver() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Id
    @Column(name = "DRIVER_ID")
    @GeneratedValue
    public long getDriverID() {
        return driverID;
    }

    public void setDriverID(long driverID) {
        this.driverID = driverID;
    }

    @ManyToOne
    @JoinColumn(name = "city_CITY_ID")
    public City getCity() { return city; }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToOne
    @JoinColumn(name = "bus_BUS_ID")
    public Bus getBus() { return bus; }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @ManyToOne
    @JoinColumn(name = "route_ROUTE_ID")
    public Route getRoute() { return route; }

    public void setRoute(Route route) {
        this.route = route;
    }
}


