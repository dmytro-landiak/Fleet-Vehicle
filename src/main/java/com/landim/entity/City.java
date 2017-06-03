package com.landim.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "CITY")
public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private long cityID;
    private String cityName;
    private BusPark busPark;

    private Set<Driver> drivers;

    public City() {}

    public City(String cityName) {
        this.cityName = cityName;
    }

    public City(String cityName, BusPark busPark) {
        this.cityName = cityName;
        this.busPark = busPark;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "CITY_ID")
    @GeneratedValue
    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    public Set<Driver> getDrivers() {
        return drivers;
    }
    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "city", cascade = CascadeType.ALL)
    public BusPark getBusPark() {
        return busPark;
    }
    public void setBusPark(BusPark busPark) {
        this.busPark = busPark;
    }
}
