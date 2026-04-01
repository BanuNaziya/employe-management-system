package com.ems.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil - a utility class that creates and provides the Hibernate SessionFactory.
 *
 * SessionFactory is a heavy object and should be created only ONCE per application.
 * We use a 'static block' so it is created when the class is first loaded.
 *
 * All DAO classes use HibernateUtil.getSessionFactory() to open a session
 * and interact with the database.
 */
public class HibernateUtil {

    // SessionFactory is shared across the entire application
    private static SessionFactory sessionFactory;

    // Static block runs once when this class is loaded
    static {
        try {
            // Load hibernate.cfg.xml from the classpath (resources folder)
            // Hibernate reads database settings and entity mappings from this file
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
            System.out.println("[Hibernate] SessionFactory created successfully.");
        } catch (Exception e) {
            // If SessionFactory fails to build, the app cannot continue
            System.out.println("[Hibernate] ERROR: Could not create SessionFactory.");
            System.out.println("  Reason: " + e.getMessage());
            System.out.println("  Please check your database connection in hibernate.cfg.xml");
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Returns the single shared SessionFactory instance.
     * Use this to open a Hibernate Session for database operations.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Closes the SessionFactory when the application shuts down.
     * Should be called once at the end of the main method.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("[Hibernate] SessionFactory closed.");
        }
    }
}
