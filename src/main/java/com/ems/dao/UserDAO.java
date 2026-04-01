package com.ems.dao;

import com.ems.config.HibernateUtil;
import com.ems.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * UserDAO - Data Access Object for User operations.
 *
 * DAO pattern separates database logic from business logic.
 * This class handles all database operations related to the 'users' table.
 *
 * Uses Hibernate Session to communicate with the database.
 */
public class UserDAO {

    /**
     * Find a user by their username.
     * Used during login to check if the user exists.
     *
     * @param username - the username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        Session session = null;
        try {
            // Open a Hibernate session (like opening a DB connection)
            session = HibernateUtil.getSessionFactory().openSession();

            // HQL (Hibernate Query Language) - similar to SQL but uses class names
            String hql = "FROM User WHERE username = :uname";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("uname", username); // prevent SQL injection

            // Returns the first result or null if not found
            return query.uniqueResult();

        } catch (Exception e) {
            System.out.println("[UserDAO] Error finding user: " + e.getMessage());
            return null;
        } finally {
            // Always close the session to release the database connection
            if (session != null) session.close();
        }
    }

    /**
     * Save a new user to the database.
     *
     * @param user - the User object to save
     * @return true if saved successfully, false if error occurred
     */
    public boolean save(User user) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Begin a transaction - all changes must be committed together
            transaction = session.beginTransaction();
            session.save(user); // insert into users table
            transaction.commit(); // save the changes to database
            return true;

        } catch (Exception e) {
            // If anything goes wrong, rollback (undo) the transaction
            if (transaction != null) transaction.rollback();
            System.out.println("[UserDAO] Error saving user: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Get all users from the database.
     * Used by ADMIN to view all accounts.
     *
     * @return List of all User objects
     */
    public List<User> getAllUsers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // "FROM User" retrieves all rows from the users table
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("[UserDAO] Error fetching users: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Check if a username already exists in the database.
     * Used before creating a new user account.
     *
     * @param username - the username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        User user = findByUsername(username);
        return user != null;
    }
}
