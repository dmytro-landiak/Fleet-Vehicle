package com.landim.entity;

import java.io.Serializable;

/**
 * Created by n0fea on 24.05.2017.
 */
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer routeID;
    private Integer firstCityID;
    private Integer secondCityID;


    public Route(Integer firstCityID, Integer secondCityID) {
        this.firstCityID = firstCityID;
        this.secondCityID = secondCityID;
    }

    public Route() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public Integer getFirstCityID() {
        return firstCityID;
    }

    public void setFirstCityID(Integer firstCityID) {
        this.firstCityID = firstCityID;
    }

    public Integer getSecondCityID() {
        return secondCityID;
    }

    public void setSecondCityID(Integer secondCityID) {
        this.secondCityID = secondCityID;
    }
}
