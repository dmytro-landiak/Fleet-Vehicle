package com.landim.crud;

import com.landim.entity.Route;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudRoute {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Route addRoute(String routeName){
        Session session = factory.openSession();
        Transaction tx = null;
        Long routeID = null;
        Route route = null;
        try{
            tx = session.beginTransaction();
            route = new Route(routeName);
            routeID = (Long) session.save(route);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return route;
    }
    public void searchRoute(Session session){
        Query query = session.createQuery("from Route where routeName = ''");
        List<Route> listRoutes = query.list();

        for (Route aRoute : listRoutes) {
            System.out.println(aRoute.getRouteName());
        }
    }
    public void listRoutes(Session session ){
        Query query = session.createQuery("from Route");
        List<Route> listRoutes = query.list();

        for (Route aRoute : listRoutes) {
            System.out.println(aRoute.getRouteName());
        }
    }
    public void updateRoute (Session session, long id_route, String routeName){
        Query query = session.createQuery("update Route set routeName = :route_name "
                + "where routeID = :idCode");
        query.setParameter("idCode", id_route);
        query.setParameter("route_name", routeName);
        query.executeUpdate();
    }
    public void deleteRoute(Session session, long id_route) {
        Query query = session.createQuery("delete Route where routeID = :idCode");
        query.setParameter("idCode", id_route);
        query.executeUpdate();
    }
}
