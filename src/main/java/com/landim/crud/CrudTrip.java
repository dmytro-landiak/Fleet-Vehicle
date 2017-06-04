package com.landim.crud;

import com.landim.entity.Trip;
import com.landim.entity.Route;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudTrip {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Long addTrip(String day, String time, int passengCount, Route route){
        Session session = factory.openSession();
        Transaction tx = null;
        Long tripID = null;
        try{
            tx = session.beginTransaction();
            Trip trip = new Trip(day, time, passengCount, route);
            tripID = (Long) session.save(trip);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return tripID;
    }
}
