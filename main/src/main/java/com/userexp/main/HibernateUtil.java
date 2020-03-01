package com.userexp.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtil {
     
    private static SessionFactory sessionFactory;
    
    static{
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }
 
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
    
    public static Session getHibernateSession() {

        final SessionFactory sf = new Configuration()
            .configure("hibernate.cfg.xml").buildSessionFactory();

        // factory = new Configuration().configure().buildSessionFactory();
        final Session session = sf.openSession();
        return session;
        }
     
    public static void shutDown(){
        //closes caches and connections
        getSessionFactory().close();
    }
}