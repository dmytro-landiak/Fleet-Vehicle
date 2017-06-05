package com.landim.crud;

import com.landim.entity.Trip;
import com.landim.entity.Route;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
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
    public void searchTrip(Session session, long id_trip){
        Query query = session.createQuery("from Trip where tripID = :idCode");
        query.setParameter("idCode", id_trip);
        List<Trip> listTrips = query.list();

        for (Trip aTrip : listTrips) {
            System.out.println(aTrip.getDay());
            System.out.println(aTrip.getTime());
            System.out.println(aTrip.getPassengCount());
        }
    }
    public void listTrips(Session session ){
        Query query = session.createQuery("from Trip");
        List<Trip> listTrips = query.list();

        for (Trip aTrip : listTrips) {
            System.out.println(aTrip.getDay());
            System.out.println(aTrip.getTime());
            System.out.println(aTrip.getPassengCount());
        }
    }
    public void updateTrip (Session session, long id_trip){
        Query query = session.createQuery("update Trip set day = :day_name, time = :trip_time, passengCount = :trip_passengCount "
                + "where tripID = :idCode");
        query.setParameter("idCode", id_trip);
        query.setParameter("day_name", "Tuesday");
        query.setParameter("trip_time", "12-00");
        query.setParameter("trip_passengCount", "60");
        query.executeUpdate();
    }
    public void deleteTrip(Session session, long id_trip) {
        Query query = session.createQuery("delete Trip where tripID = :idCode");
        query.setParameter("idCode", id_trip);
        query.executeUpdate();
    }
    public ArrayList selectForForecast(Session session, String trip_day, String trip_time, String route_name){
        List<Integer> selectedTrips = new ArrayList<Integer>();
        Query query = session.createQuery("from Trip where day = :trip_day AND time = :trip_time AND route.routeName = :route_name");
        query.setParameter("trip_day", trip_day);
        query.setParameter("trip_time", trip_time);
        query.setParameter("route_name", route_name);

        List <Trip> listTrips = query.list();
        for (Trip aTrip : listTrips) {
            selectedTrips.add(aTrip.getPassengCount());
        }
        return (ArrayList) selectedTrips;
    }
}
