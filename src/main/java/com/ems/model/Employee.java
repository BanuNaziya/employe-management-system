package com.ems.model;

import javax.persistence.*;

/**
 * Employee entity - stores the employee's personal and job-related information.
 * This class is mapped to the 'employees' table in the database using Hibernate.
 *
 * Each employee is optionally linked to a User account (for login purposes).
 */
@Entity
@Table(name = "employees")
public class Employee {

    // Primary key - auto incremented by database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Full name of the employee
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // Employee's email address - must be unique
    @Column(name = "email", unique = true, length = 100)
    private String email;

    // Department the employee belongs to (e.g., IT, HR, Finance)
    @Column(name = "department", length = 50)
    private String department;

    // Monthly or annual salary of the employee
    @Column(name = "salary")
    private double salary;

    // Contact phone number
    @Column(name = "phone", length = 15)
    private String phone;

    // Link to the User account (optional - not every employee needs a login)
    // @JoinColumn specifies the foreign key column in the employees table
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ===== Constructors =====

    // Default constructor required by Hibernate
    public Employee() {}

    // Constructor to quickly create an employee
    public Employee(String name, String email, String department, double salary, String phone) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.phone = phone;
    }

    // ===== Getters and Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // toString - prints a clean summary of employee details
    @Override
    public String toString() {
        return "\n-----------------------------" +
               "\n  ID         : " + id +
               "\n  Name       : " + name +
               "\n  Email      : " + email +
               "\n  Department : " + department +
               "\n  Salary     : " + salary +
               "\n  Phone      : " + phone +
               "\n-----------------------------";
    }
}
