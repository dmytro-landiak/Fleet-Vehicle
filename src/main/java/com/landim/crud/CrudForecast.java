package com.landim.crud;

import com.landim.entity.Prognos;
import com.landim.entity.Route;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudForecast {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Long addForecast(String day, String time, int prognosCount, Route route){
        Session session = factory.openSession();
        Transaction tx = null;
        Long prognosID = null;
        try{
            tx = session.beginTransaction();
            Prognos forecast = new Prognos(day, time, prognosCount, route);
            prognosID = (Long) session.save(forecast);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return prognosID;
    }
    public void searchForecast(Session session, long id_prognos){
        Query query = session.createQuery("from Prognos where prognosID = :idCode");
        query.setParameter("idCode", id_prognos);
        List<Prognos> listForecasts = query.list();

        for (Prognos aForecast : listForecasts) {
            System.out.println(aForecast.getDay());
            System.out.println(aForecast.getTime());
            System.out.println(aForecast.getPrognosCount());
        }
    }
    public void listForecasts(Session session ){
        Query query = session.createQuery("from Prognos ");
        List<Prognos> listForecasts = query.list();

        for (Prognos aForecast : listForecasts) {
            System.out.println(aForecast.getDay());
            System.out.println(aForecast.getTime());
            System.out.println(aForecast.getPrognosCount());
        }
    }
    public void updateForecast (Session session, long id_prognos){
        Query query = session.createQuery("update Prognos set day = :day_name, time = :prognos_time, prognosCount = :prognos_passengCount "
                + "where prognosID = :idCode");
        query.setParameter("idCode", id_prognos);
        query.setParameter("day_name", "Tuesday");
        query.setParameter("prognos_time", "12-00");
        query.setParameter("prognos_passengCount", "60");
        query.executeUpdate();
    }
    public void deleteForecast(Session session, long id_prognos) {
        Query query = session.createQuery("delete Prognos where prognosID = :idCode");
        query.setParameter("idCode", id_prognos);
        query.executeUpdate();
    }

}
