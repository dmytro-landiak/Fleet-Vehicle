package com.landim.crud;

import com.landim.entity.City;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.Iterator;
import java.util.List;
import static java.lang.System.*;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudCity {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }
    public Long addCity(String cityName){
        Session session = factory.openSession();
        Transaction tx = null;
        Long cityID = null;
        try{
            tx = session.beginTransaction();
            City city = new City(cityName);
            cityID = (Long) session.save(city);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return cityID;
    }
    public void listCities( ){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List cities = session.createQuery("FROM City ").list();
            for (Iterator iterator = cities.iterator(); iterator.hasNext();){
                City city = (City) iterator.next();
                out.print("City name: " + city.getCityName());
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
    public void updateCity (Session session, Integer id_city){
            Query query = session.createQuery("update City set cityName = :city_name "
                    + "where cityID = :idCode");
            query.setParameter("idCode", id_city);
            query.setParameter("city_name", "Lviv");
            query.executeUpdate();
    }
    public void deleteCity(Integer cityID){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            City city =
                    (City)session.get(City.class, cityID);
            session.delete(city);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
