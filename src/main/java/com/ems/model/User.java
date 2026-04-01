package com.ems.model;

import javax.persistence.*;

/**
 * User entity - represents a login account in the system.
 * Each user has a role: ADMIN or EMPLOYEE.
 * ADMIN can manage all employees.
 * EMPLOYEE can only view their own profile.
 *
 * This class is mapped to the 'users' table in the database using Hibernate.
 */
@Entity
@Table(name = "users")
public class User {

    // Primary key - auto incremented by database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Username must be unique - used for login
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    // Password stored as plain text (in real projects, always hash passwords)
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // Role: either "ADMIN" or "EMPLOYEE"
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    // One User can have one Employee record linked to them
    // mappedBy means the 'user' field in Employee class owns the relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Employee employee;

    // ===== Constructors =====

    // Default constructor required by Hibernate
    public User() {}

    // Constructor to create a new user quickly
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ===== Getters and Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    // toString - useful for printing user info (does not print password)
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
