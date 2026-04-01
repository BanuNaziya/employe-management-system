package com.ems;

import com.ems.config.HibernateUtil;
import com.ems.dao.UserDAO;
import com.ems.model.Employee;
import com.ems.model.User;
import com.ems.service.AuthService;
import com.ems.service.EmployeeService;

import java.util.List;
import java.util.Scanner;

/**
 * Main.java - Entry point of the Employee Management System.
 *
 * This is a console-based application.
 * When you run it:
 *   1. It connects to MySQL database via Hibernate
 *   2. Prompts for login
 *   3. Shows a menu based on user's role (ADMIN or EMPLOYEE)
 *
 * Role-Based Access Control (RBAC):
 *   ADMIN    -> Full CRUD on employees, manage user accounts
 *   EMPLOYEE -> View their own profile only
 *
 * How to run:
 *   mvn exec:java   OR   java -jar target/employee-management-system-1.0-jar-with-dependencies.jar
 */
public class Main {

    // Scanner reads input from keyboard (System.in)
    static Scanner scanner = new Scanner(System.in);

    // Services handle business logic
    static AuthService authService = new AuthService();
    static EmployeeService employeeService = new EmployeeService();

    public static void main(String[] args) {

        printBanner();

        // Check if default admin exists; create if not (first time setup)
        setupDefaultAdmin();

        // Keep showing login until successful
        User loggedInUser = null;
        while (loggedInUser == null) {
            loggedInUser = loginScreen();
            if (loggedInUser == null) {
                System.out.println("  Invalid credentials. Please try again.\n");
            }
        }

        System.out.println("\n  Welcome, " + loggedInUser.getUsername() +
                           "! [Role: " + loggedInUser.getRole() + "]");

        // Show different menu based on role
        if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            adminMenu(loggedInUser);
        } else {
            employeeMenu(loggedInUser);
        }

        // Close Hibernate session factory when done
        HibernateUtil.shutdown();
        System.out.println("\n  Goodbye!");
    }

    // ============================================================
    //  SETUP - Create default admin account on first run
    // ============================================================

    /**
     * Creates a default ADMIN account if no users exist in the database.
     * Default credentials: admin / admin123
     * Admin should change this password after first login.
     */
    static void setupDefaultAdmin() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();

        if (users == null || users.isEmpty()) {
            System.out.println("  First time setup: Creating default admin account...");
            authService.register("admin", "admin123", "ADMIN");
            System.out.println("  Default admin created. Username: admin | Password: admin123");
            System.out.println("  Please change the password after login.\n");
        }
    }

    // ============================================================
    //  LOGIN SCREEN
    // ============================================================

    /**
     * Shows the login prompt and returns the logged-in User.
     * Returns null if credentials are wrong.
     */
    static User loginScreen() {
        System.out.println("=========================================");
        System.out.println("          EMPLOYEE MANAGEMENT SYSTEM     ");
        System.out.println("=========================================");
        System.out.print("  Username : ");
        String username = scanner.nextLine().trim();

        System.out.print("  Password : ");
        String password = scanner.nextLine().trim();

        return authService.login(username, password);
    }

    // ============================================================
    //  ADMIN MENU
    // ============================================================

    /**
     * Shows the admin menu with full CRUD options.
     * Loops until admin chooses to logout.
     */
    static void adminMenu(User admin) {
        boolean running = true;

        while (running) {
            System.out.println("\n=========================================");
            System.out.println("            ADMIN MENU                   ");
            System.out.println("=========================================");
            System.out.println("  1. Add Employee");
            System.out.println("  2. View All Employees");
            System.out.println("  3. Search Employee by ID");
            System.out.println("  4. Search Employees by Department");
            System.out.println("  5. Update Employee");
            System.out.println("  6. Delete Employee");
            System.out.println("  7. Create User Account");
            System.out.println("  0. Logout");
            System.out.println("-----------------------------------------");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addEmployee(); break;
                case "2": viewAllEmployees(); break;
                case "3": searchById(); break;
                case "4": searchByDepartment(); break;
                case "5": updateEmployee(); break;
                case "6": deleteEmployee(); break;
                case "7": createUserAccount(); break;
                case "0":
                    System.out.println("\n  Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("  Invalid choice. Enter a number from the menu.");
            }
        }
    }

    // ============================================================
    //  EMPLOYEE MENU
    // ============================================================

    /**
     * Shows the employee menu (limited access).
     * Employee can only view their own profile.
     */
    static void employeeMenu(User user) {
        boolean running = true;

        while (running) {
            System.out.println("\n=========================================");
            System.out.println("            EMPLOYEE MENU                ");
            System.out.println("=========================================");
            System.out.println("  1. View My Profile");
            System.out.println("  0. Logout");
            System.out.println("-----------------------------------------");
            System.out.print("  Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Fetch employee profile linked to this user account
                    Employee profile = employeeService.getMyProfile(user.getId());
                    if (profile != null) {
                        System.out.println("\n  Your Profile:");
                        System.out.println(profile);
                    }
                    break;
                case "0":
                    System.out.println("\n  Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("  Invalid choice.");
            }
        }
    }

    // ============================================================
    //  ADMIN ACTIONS
    // ============================================================

    /** Add a new employee by collecting details from the user. */
    static void addEmployee() {
        System.out.println("\n  --- Add New Employee ---");

        System.out.print("  Name       : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Email      : ");
        String email = scanner.nextLine().trim();

        System.out.print("  Department : ");
        String dept = scanner.nextLine().trim();

        System.out.print("  Salary     : ");
        double salary = 0;
        try {
            salary = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Invalid salary. Setting to 0.");
        }

        System.out.print("  Phone      : ");
        String phone = scanner.nextLine().trim();

        // Ask if this employee should have a login account
        System.out.print("  Create login account for this employee? (yes/no): ");
        String createAccount = scanner.nextLine().trim();

        Employee employee = new Employee(name, email, dept, salary, phone);

        if ("yes".equalsIgnoreCase(createAccount)) {
            System.out.print("  Username for login : ");
            String username = scanner.nextLine().trim();
            System.out.print("  Password for login : ");
            String password = scanner.nextLine().trim();

            // Register user account first
            boolean registered = authService.register(username, password, "EMPLOYEE");
            if (registered) {
                // Link the user account to this employee
                UserDAO userDAO = new UserDAO();
                User newUser = userDAO.findByUsername(username);
                employee.setUser(newUser);
            }
        }

        // Save the employee record
        employeeService.addEmployee(employee);
    }

    /** Display all employees in a formatted list. */
    static void viewAllEmployees() {
        System.out.println("\n  --- All Employees ---");
        List<Employee> employees = employeeService.getAllEmployees();

        if (employees != null && !employees.isEmpty()) {
            System.out.println("  Total: " + employees.size() + " employee(s)\n");
            for (Employee emp : employees) {
                System.out.println(emp); // calls Employee.toString()
            }
        }
    }

    /** Search and display a single employee by their ID. */
    static void searchById() {
        System.out.println("\n  --- Search Employee by ID ---");
        System.out.print("  Enter Employee ID: ");

        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Employee emp = employeeService.getEmployeeById(id);
            if (emp != null) {
                System.out.println(emp);
            }
        } catch (NumberFormatException e) {
            System.out.println("  Invalid ID. Please enter a number.");
        }
    }

    /** Search and display employees by department name. */
    static void searchByDepartment() {
        System.out.println("\n  --- Search by Department ---");
        System.out.print("  Enter Department name: ");
        String dept = scanner.nextLine().trim();

        List<Employee> employees = employeeService.getByDepartment(dept);
        if (employees != null && !employees.isEmpty()) {
            System.out.println("  Found " + employees.size() + " employee(s) in '" + dept + "':");
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
    }

    /** Update an existing employee's details. Skip a field to keep existing value. */
    static void updateEmployee() {
        System.out.println("\n  --- Update Employee ---");
        System.out.print("  Enter Employee ID to update: ");

        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            // Show current details before updating
            Employee existing = employeeService.getEmployeeById(id);
            if (existing == null) return;

            System.out.println("  Current details: " + existing);
            System.out.println("  (Press Enter to keep the existing value)\n");

            System.out.print("  New Name [" + existing.getName() + "]: ");
            String name = scanner.nextLine().trim();

            System.out.print("  New Email [" + existing.getEmail() + "]: ");
            String email = scanner.nextLine().trim();

            System.out.print("  New Department [" + existing.getDepartment() + "]: ");
            String dept = scanner.nextLine().trim();

            System.out.print("  New Salary [" + existing.getSalary() + "]: ");
            String salaryStr = scanner.nextLine().trim();
            double salary = salaryStr.isEmpty() ? -1 : Double.parseDouble(salaryStr);

            System.out.print("  New Phone [" + existing.getPhone() + "]: ");
            String phone = scanner.nextLine().trim();

            // Pass null for fields that were left empty (they won't be updated)
            employeeService.updateEmployee(
                id,
                name.isEmpty() ? null : name,
                email.isEmpty() ? null : email,
                dept.isEmpty() ? null : dept,
                salary,
                phone.isEmpty() ? null : phone
            );

        } catch (NumberFormatException e) {
            System.out.println("  Invalid input. Please enter valid values.");
        }
    }

    /** Delete an employee by their ID. Asks for confirmation before deleting. */
    static void deleteEmployee() {
        System.out.println("\n  --- Delete Employee ---");
        System.out.print("  Enter Employee ID to delete: ");

        try {
            int id = Integer.parseInt(scanner.nextLine().trim());

            // Show employee details and confirm before deleting
            Employee emp = employeeService.getEmployeeById(id);
            if (emp == null) return;

            System.out.println("  You are about to delete: " + emp.getName());
            System.out.print("  Are you sure? (yes/no): ");
            String confirm = scanner.nextLine().trim();

            if ("yes".equalsIgnoreCase(confirm)) {
                employeeService.deleteEmployee(id);
            } else {
                System.out.println("  Delete cancelled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("  Invalid ID. Please enter a number.");
        }
    }

    /** Create a new user login account (ADMIN only). */
    static void createUserAccount() {
        System.out.println("\n  --- Create User Account ---");
        System.out.print("  Username : ");
        String username = scanner.nextLine().trim();

        System.out.print("  Password : ");
        String password = scanner.nextLine().trim();

        System.out.print("  Role (ADMIN/EMPLOYEE) : ");
        String role = scanner.nextLine().trim().toUpperCase();

        // Validate role input
        if (!role.equals("ADMIN") && !role.equals("EMPLOYEE")) {
            System.out.println("  Invalid role. Must be ADMIN or EMPLOYEE.");
            return;
        }

        authService.register(username, password, role);
    }

    /** Print a welcome banner when the app starts. */
    static void printBanner() {
        System.out.println("=========================================");
        System.out.println("   EMPLOYEE MANAGEMENT SYSTEM v1.0       ");
        System.out.println("   Java | Hibernate | JDBC | MySQL        ");
        System.out.println("=========================================\n");
    }
}
