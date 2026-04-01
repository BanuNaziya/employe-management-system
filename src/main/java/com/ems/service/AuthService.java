package com.ems.service;

import com.ems.dao.UserDAO;
import com.ems.model.User;

/**
 * AuthService - handles user authentication (login).
 *
 * This service layer sits between the main program and the DAO layer.
 * It contains the business logic for verifying login credentials.
 *
 * Role-Based Access Control (RBAC):
 *   - ADMIN : can add, view, update, delete all employees
 *   - EMPLOYEE : can only view their own profile
 */
public class AuthService {

    // DAO object used to fetch user data from the database
    private UserDAO userDAO = new UserDAO();

    /**
     * Login - validate username and password.
     *
     * Steps:
     * 1. Look up the user by username in the database
     * 2. If found, check if the password matches
     * 3. If match, return the User (login success)
     * 4. If not found or password wrong, return null (login failed)
     *
     * @param username - username entered by the user
     * @param password - password entered by the user
     * @return User object if login is successful, null otherwise
     */
    public User login(String username, String password) {
        // Find user from the database
        User user = userDAO.findByUsername(username);

        if (user == null) {
            // No account found with that username
            return null;
        }

        // Check if the provided password matches the stored password
        if (user.getPassword().equals(password)) {
            return user; // Login successful
        }

        // Password did not match
        return null;
    }

    /**
     * Register a new user account.
     * Only ADMIN can create new accounts.
     *
     * @param username - desired username
     * @param password - desired password
     * @param role     - "ADMIN" or "EMPLOYEE"
     * @return true if account created, false if username already taken or error
     */
    public boolean register(String username, String password, String role) {
        // Check if username is already taken
        if (userDAO.usernameExists(username)) {
            System.out.println("  Username '" + username + "' is already taken. Choose another.");
            return false;
        }

        // Create new User object and save it
        User newUser = new User(username, password, role);
        boolean saved = userDAO.save(newUser);

        if (saved) {
            System.out.println("  User account created: " + username + " [" + role + "]");
        }
        return saved;
    }

    /**
     * Check if the logged-in user has ADMIN role.
     * Used to restrict certain actions to admins only.
     *
     * @param user - the currently logged-in user
     * @return true if the user is an ADMIN
     */
    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
}
