package com.ems.dao;

import com.ems.config.HibernateUtil;
import com.ems.model.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

/**
 * EmployeeDAO - Data Access Object for Employee CRUD operations.
 *
 * Handles all database interactions for the 'employees' table.
 * Uses Hibernate ORM to map Java objects to database rows.
 *
 * CRUD = Create, Read, Update, Delete
 */
public class EmployeeDAO {

    /**
     * CREATE - Add a new employee to the database.
     *
     * @param employee - Employee object with details filled in
     * @return true if added successfully, false on error
     */
    public boolean addEmployee(Employee employee) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // session.save() inserts a new row into the employees table
            session.save(employee);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("[EmployeeDAO] Error adding employee: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * READ - Get all employees from the database.
     * This is an optimized query - only fetches required columns.
     *
     * @return List of all Employee objects
     */
    public List<Employee> getAllEmployees() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // HQL query to fetch all employees - Hibernate converts this to SQL
            // ORDER BY id makes the results predictable and easy to read
            Query<Employee> query = session.createQuery("FROM Employee ORDER BY id", Employee.class);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("[EmployeeDAO] Error fetching employees: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * READ - Get a single employee by their ID.
     *
     * @param id - the employee's unique ID
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(int id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // session.get() fetches a record by primary key
            // Returns null if no record with that id exists
            return session.get(Employee.class, id);

        } catch (Exception e) {
            System.out.println("[EmployeeDAO] Error finding employee by ID: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * READ - Find the employee linked to a specific user account.
     * Used when an EMPLOYEE logs in - to show their own profile.
     *
     * @param userId - the user's ID
     * @return Employee linked to that user, or null if not found
     */
    public Employee getEmployeeByUserId(int userId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Query employees where the linked user's id matches
            String hql = "FROM Employee e WHERE e.user.id = :uid";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("uid", userId);
            return query.uniqueResult();

        } catch (Exception e) {
            System.out.println("[EmployeeDAO] Error finding employee by user ID: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * READ - Search employees by department name.
     * Demonstrates an optimized query - filters at database level.
     *
     * @param department - department name to filter by
     * @return List of employees in that department
     */
    public List<Employee> getEmployeesByDepartment(String department) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // LIKE operator for case-insensitive partial match
            String hql = "FROM Employee WHERE LOWER(department) = LOWER(:dept)";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("dept", department);
            return query.getResultList();

        } catch (Exception e) {
            System.out.println("[EmployeeDAO] Error searching by department: " + e.getMessage());
            return null;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * UPDATE - Update an existing employee's details.
     *
     * @param employee - Employee object with updated values (must have correct id)
     * @return true if updated successfully, false on error
     */
    public boolean updateEmployee(Employee employee) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // session.update() updates the row that matches the employee's id
            session.update(employee);
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("[EmployeeDAO] Error updating employee: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * DELETE - Remove an employee from the database by ID.
     *
     * @param id - the ID of the employee to delete
     * @return true if deleted successfully, false if not found or error
     */
    public boolean deleteEmployee(int id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // First fetch the employee, then delete them
            Employee employee = session.get(Employee.class, id);
            if (employee == null) {
                System.out.println("[EmployeeDAO] No employee found with ID: " + id);
                return false;
            }

            session.delete(employee); // delete the row from database
            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("[EmployeeDAO] Error deleting employee: " + e.getMessage());
            return false;
        } finally {
            if (session != null) session.close();
        }
    }
}
