package com.landim.crud;

import com.landim.entity.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.Iterator;
import java.util.List;
import static java.lang.System.*;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudRoute {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Integer addUser(String name, String eMail, String password){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer userID = null;
        try{
            tx = session.beginTransaction();
            User user = new User(name, eMail, password);
            userID = (Integer) session.save(user);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return userID;
    }
    /* Method to  READ all the employees */
    public void listUser( ){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List users = session.createQuery("FROM User ").list();
            for (Iterator iterator =
                 users.iterator(); iterator.hasNext();){
                User user = (User) iterator.next();
                out.print("Name: " + user.getName());
                out.print("  E-mail: " + user.geteMail());
                out.println("  password: " + user.getPassword());
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }

    public void updateUser (Session session, Integer id_user){
        session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Query query = session.createQuery("update User set name = :user_name, eMail = :user_eMail, password = :user_password "
                    + "where userID = :idCode");
            query.setParameter("idCode", id_user);
            query.setParameter("user_name", "Mark");
            query.setParameter("user_eMail", "mark@gmail.com");
            query.setParameter("user_password", "6789");
            query.executeUpdate();

            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }

    }

    /* Method to DELETE an employee from the records */
    public void deleteUser(Integer userID){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            User user =
                    (User)session.get(User.class, userID);
            session.delete(user);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
}
