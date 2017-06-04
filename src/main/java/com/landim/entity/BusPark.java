package com.landim.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "BUSPARK")
public class BusPark implements Serializable {
    private static final long serialVersionUID = 1L;

    private long busParkID;
    private String busParkName;
    private City city;

    private Set<BusParkBusType> busParkBusTypes = new HashSet<BusParkBusType>();
    private Set<Bus> buses;

    public BusPark(String busParkName) {
        this.busParkName = busParkName;
    }

    public BusPark(String busParkName, City city) {
        this.busParkName = busParkName;
        this.city = city;
    }

    public void addBusType(BusParkBusType busType) {
        this.busParkBusTypes.add(busType);
    }

    public BusPark() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "BUSPARK_ID")
    @GeneratedValue
    public long getBusParkID() {
        return busParkID;
    }

    public void setBusParkID(long busParkID) {
        this.busParkID = busParkID;
    }

    public String getBusParkName() {
        return busParkName;
    }

    public void setBusParkName(String busParkName) {
        this.busParkName = busParkName;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public City getCity() { return this.city; }
    public void setCity(City city) {
        this.city = city;
    }

    @OneToMany(mappedBy = "busPark", cascade = CascadeType.ALL)
    public Set<Bus> getBuses() {
        return buses;
    }
    public void setBuses(Set<Bus> buses) {
        this.buses = buses;
    }

    @OneToMany(mappedBy = "primaryKey.busPark",
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
