package com.landim.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class BusParkTypeId implements Serializable {
    private BusPark busPark;
    private BusType busType;

    @ManyToOne(cascade = CascadeType.ALL)
    public BusPark getBusPark() {
        return busPark;
    }

    public void setBusPark(BusPark busPark) {
        this.busPark = busPark;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }
}