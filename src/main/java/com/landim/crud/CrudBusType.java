package com.landim.crud;

import com.landim.entity.BusType;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudBusType {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Long addBusType(String type, int capacity){
        Session session = factory.openSession();
        Transaction tx = null;
        Long busTypeId = null;
        try{
            tx = session.beginTransaction();
            BusType busType = new BusType(type, capacity);
            busTypeId = (Long) session.save(busType);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return busTypeId;
    }
    public void searchBusType(Session session){
        Query query = session.createQuery("from BusType where type = ''");
        List<BusType> listBusTypes = query.list();

        for (BusType aBusType : listBusTypes) {
            System.out.println(aBusType.getType());
        }
    }
    public void listBusTypes(Session session ){
        Query query = session.createQuery("from BusType ");
        List<BusType> listBusTypes = query.list();

        for (BusType aBusType : listBusTypes) {
            System.out.println(aBusType.getType());
        }
    }
    public void updateBusType (Session session, long id_busType, int capacity){
        Query query = session.createQuery("update BusType set capacity = :busType_capacity "
                + "where busTypeID = :idCode");
        query.setParameter("idCode", id_busType);
        query.setParameter("busType_capacity", capacity);
        query.executeUpdate();
    }
    public void deleteBusType(Session session, long id_busType) {
        Query query = session.createQuery("delete BusType where busTypeID = :idCode");
        query.setParameter("idCode", id_busType);
        query.executeUpdate();
    }
}
