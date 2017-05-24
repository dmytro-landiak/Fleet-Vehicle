package com.landim.crud;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

import com.landim.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EnterPoint {
    private static SessionFactory factory;
    public static void main(String[] args) {
        try{
            factory = new Configuration().configure().buildSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        EnterPoint ME = new EnterPoint();

      /* Add few employee records in database */
        Integer empID1 = ME.addUser("Zara", "Ali", "qwe");
        Integer empID2 = ME.addUser("Daisy", "Das", "5000");
        Integer empID3 = ME.addUser("John", "Paul", "31321");

      /* List down all the employees */
        ME.listUser();

      /* Update employee's records */
        ME.updateUser(empID1, "wtf");

      /* Delete an employee from the database */
        ME.deleteUser(empID2);
        ME.deleteUser(empID1);

      /* List down new list of the employees */
        ME.listUser();
    }
    /* Method to CREATE an employee in the database */
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
                System.out.print("Name: " + user.getName());
                System.out.print("  E-mail: " + user.geteMail());
                System.out.println("  password: " + user.getPassword());
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
    }
    /* Method to UPDATE salary for an employee */
    public void updateUser(Integer userID, String eMail ){
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            User user =
                    (User)session.get(User.class, userID);
            user.seteMail( eMail );
            session.update(user);
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