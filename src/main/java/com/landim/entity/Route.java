package com.landim.entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "ROUTE")
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;

    private long routeID;
    private String routeName;

    private Set<Driver> drivers;
    private Set<Trip> trips;
    private Set<Prognos> prognoses;

    public Route(String routeName) {
        this.routeName = routeName;
    }

    public Route() {}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "ROUTE_ID")
    @GeneratedValue
    public long getRouteID() {
        return routeID;
    }

    public void setRouteID(long routeID) {
        this.routeID = routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    public Set<Driver> getDrivers() {
        return drivers;
    }
    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    public Set<Trip> getTrips() {
        return trips;
    }
    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    public Set<Prognos> getPrognoses() {
        return prognoses;
    }
    public void setPrognoses(Set<Prognos> prognoses) {
        this.prognoses = prognoses;
    }

}
