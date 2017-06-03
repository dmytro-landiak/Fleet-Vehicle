package com.landim.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by n0fea on 24.05.2017.
 */
@Entity
@Table(name = "BUSPARK_BUS_TYPE")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.buspark",
                joinColumns = @JoinColumn(name = "BUSPARK_ID")),
        @AssociationOverride(name = "primaryKey.bus_type",
                joinColumns = @JoinColumn(name = "BUSTYPE_ID")) })
public class BusParkBusType implements Serializable {
    private static final long serialVersionUID = 1L;

    private BusParkTypeId primaryKey = new BusParkTypeId();

    private Integer count;

    @EmbeddedId
    public BusParkTypeId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(BusParkTypeId primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Transient
    public BusPark getBusPark() {
        return getPrimaryKey().getBusPark();
    }

    public void setBusPark(BusPark busPark) {
        getPrimaryKey().setBusPark(busPark);
    }

    @Transient
    public BusType getBusType() {
        return getPrimaryKey().getBusType();
    }

    public void setBusType(BusType busType) {
        getPrimaryKey().setBusType(busType);
    }

    @Column(name = "COUNT")
    public Integer getCount(){
        return count;
    }

    public void setCount(Integer count){
        this.count = count;
    }
}
