package com.landim.crud;

import com.landim.entity.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Created by n0fea on 29.05.2017.
 */
public class CrudUser {
    private static SessionFactory factory;

    static {
        factory = new Configuration().configure().buildSessionFactory();
    }

    public Long addUser(String name, String eMail, String password){
        Session session = factory.openSession();
        Transaction tx = null;
        Long userID = null;
        try{
            tx = session.beginTransaction();
            User user = new User(name, eMail, password);
            userID = (Long) session.save(user);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }finally {
            session.close();
        }
        return userID;
    }
    public void searchUser(Session session){
        Query query = session.createQuery("from User where name = 'Vlad'");
        List<User> listUsers = query.list();

        for (User aUser : listUsers) {
            System.out.println(aUser.geteMail());
        }
    }
    public void listUsers(Session session ){
        Query query = session.createQuery("from User");
        List<User> listUsers = query.list();

        for (User aUser : listUsers) {
            System.out.println(aUser.getName());
        }
    }
    public void updateUser (Session session, long id_user){
        Query query = session.createQuery("update User set name = :user_name, eMail = :user_eMail, password = :user_password "
                + "where userID = :idCode");
        query.setParameter("idCode", id_user);
        query.setParameter("user_name", "Mark");
        query.setParameter("user_eMail", "mark@gmail.com");
        query.setParameter("user_password", "6789");
        query.executeUpdate();
    }
    public void deleteUser(Session session, long id_user) {
        Query query = session.createQuery("delete User where userID = :idCode");
        query.setParameter("idCode", id_user);
        query.executeUpdate();
    }
}
