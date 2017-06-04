package com.landim.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "BUS")
public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;

    private long busID;
    private String registPlate;
    private BusPark busPark;
    private BusType busType;

    private Set<Driver> drivers;

    public Bus(String registPlate) {
        this.registPlate = registPlate;
    }

    public Bus(String registPlate, BusPark busPark, BusType busType) {
        this.registPlate = registPlate;
        this.busPark = busPark;
        this.busType = busType;
    }

    public Bus() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "BUS_ID")
    @GeneratedValue
    public long getBusID() {
        return busID;
    }

    public void setBusID(long busID) {
        this.busID = busID;
    }

    @Column(name = "REGIST_PLATE")
    public String getRegistPlate() {
        return registPlate;
    }

    public void setRegistPlate(String registPlate) {
        this.registPlate = registPlate;
    }

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    public Set<Driver> getDrivers() {
        return drivers;
    }
    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    @ManyToOne
    @JoinColumn(name = "buspark_BUSPARK_ID")
    public BusPark getBusPark() { return busPark; }
    public void setBusPark(BusPark busPark) {
        this.busPark = busPark;
    }

    @ManyToOne
    @JoinColumn(name = "bus_type_BUSTYPE_ID")
    public BusType getBusType() { return busType; }
    public void setBusType(BusType busType) {
        this.busType = busType;
    }
}