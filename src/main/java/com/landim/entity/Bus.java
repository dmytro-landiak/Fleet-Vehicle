package com.landim.entity;

import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
public class Bus implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer busID;
    private String type;
    private Integer capacity;
    private String registPlate;



    public Bus(String type, Integer capacity, String registPlate) {
        this.type = type;
        this.capacity = capacity;
        this.registPlate = registPlate;
    }

    public Bus() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getBusID() {
        return busID;
    }

    public void setBusID(Integer busID) {
        this.busID = busID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRegistPlate() {
        return registPlate;
    }

    public void setRegistPlate(String registPlate) {
        this.registPlate = registPlate;
    }
}