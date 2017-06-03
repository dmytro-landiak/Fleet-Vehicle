package com.landim.crud;

import com.landim.entity.City;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

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
    public void searchCity(Session session){
        Query query = session.createQuery("from City where cityName = ''");
        List<City> listCities = query.list();

        for (City aCity : listCities) {
            System.out.println(aCity.getCityName());
        }
    }
    public void listCities(Session session ){
        Query query = session.createQuery("from City");
        List<City> listCities = query.list();

        for (City aCity : listCities) {
            System.out.println(aCity.getCityName());
        }
    }
    public void updateCity (Session session, long id_city, String cityName){
        Query query = session.createQuery("update City set cityName = :city_name "
                + "where cityID = :idCode");
        query.setParameter("idCode", id_city);
        query.setParameter("city_name", cityName);
        query.executeUpdate();
    }
    public void deleteCity(Session session, long id_city) {
        Query query = session.createQuery("delete City where cityID = :idCode");
        query.setParameter("idCode", id_city);
        query.executeUpdate();
    }
}
