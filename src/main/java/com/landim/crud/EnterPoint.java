package com.landim.crud;

import com.landim.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class EnterPoint {
    private static SessionFactory factory = null;

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        CrudUser users = new CrudUser();

        //users.addUser("Tom", "tom@gmail.com", "0000");

        users.updateUser(session, 76);
        users.listUser();
        tx.commit();
        session.close();
    }
}