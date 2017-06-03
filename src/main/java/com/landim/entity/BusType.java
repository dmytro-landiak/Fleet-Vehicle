package com.landim.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "BUS_TYPE")
public class BusType implements Serializable {
    private static final long serialVersionUID = 1L;

    private long busTypeID;
    private String type;
    private Integer capacity;

    private Set<Bus> buses;
    private Set<BusParkBusType> busParkBusTypes = new HashSet<BusParkBusType>();

    public BusType(String type, Integer capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    public BusType() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "BUSTYPE_ID")
    @GeneratedValue
    public long getBusTypeID() {
        return busTypeID;
    }

    public void setBusTypeID(long busTypeID) {
        this.busTypeID = busTypeID;
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

    @OneToMany(mappedBy = "bustype", cascade = CascadeType.ALL)
    public Set<Bus> getBuses() {
        return buses;
    }
    public void setBuses(Set<Bus> buses) {
        this.buses = buses;
    }

    @OneToMany(mappedBy = "primaryKey.bus_type",
            cascade = CascadeType.ALL)
    public Set<BusParkBusType> getBusParkBusTypes() {
        return busParkBusTypes;
    }
    public void setBusParkBusTypes(Set<BusParkBusType> busTypes) {
        this.busParkBusTypes = busTypes;
    }
    public void addBusParkBusType(BusParkBusType busParkBusType) {
        this.busParkBusTypes.add(busParkBusType);
    }
}