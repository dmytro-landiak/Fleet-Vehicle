package com.landim.entity;

import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer driverID;
    private String driverName;
    private String phone;


    public Driver(String driverName, String phone) {
        this.driverName = driverName;
        this.phone = phone;
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

    public Integer getDriverID() {
        return driverID;
    }

    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
    }

}


