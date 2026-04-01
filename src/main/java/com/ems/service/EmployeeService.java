package com.ems.service;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;

import java.util.List;

/**
 * EmployeeService - business logic layer for employee operations.
 *
 * This class sits between Main.java and EmployeeDAO.java.
 * It handles input validation and calls the appropriate DAO methods.
 *
 * Design pattern: Service layer delegates actual database work to DAO.
 */
public class EmployeeService {

    // DAO object - handles all database operations for employees
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * Add a new employee.
     * Validates that name and email are not empty before saving.
     *
     * @param employee - Employee object to add
     * @return true if added successfully
     */
    public boolean addEmployee(Employee employee) {
        // Basic validation - name and email are required fields
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            System.out.println("  Error: Employee name cannot be empty.");
            return false;
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            System.out.println("  Error: Employee email cannot be empty.");
            return false;
        }

        // Delegate to DAO for actual database insert
        boolean added = employeeDAO.addEmployee(employee);
        if (added) {
            System.out.println("  Employee '" + employee.getName() + "' added successfully!");
        } else {
            System.out.println("  Failed to add employee. Email may already exist.");
        }
        return added;
    }

    /**
     * Get all employees from the database.
     * Displays a message if no employees are found.
     *
     * @return List of employees (may be empty)
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();

        if (employees == null || employees.isEmpty()) {
            System.out.println("  No employees found in the system.");
        }
        return employees;
    }

    /**
     * Get one employee by ID.
     *
     * @param id - employee ID to look up
     * @return Employee object, or null if not found
     */
    public Employee getEmployeeById(int id) {
        Employee employee = employeeDAO.getEmployeeById(id);
        if (employee == null) {
            System.out.println("  No employee found with ID: " + id);
        }
        return employee;
    }

    /**
     * Get the employee profile linked to a user account.
     * Used when EMPLOYEE role logs in.
     *
     * @param userId - the logged-in user's ID
     * @return Employee linked to that user
     */
    public Employee getMyProfile(int userId) {
        Employee employee = employeeDAO.getEmployeeByUserId(userId);
        if (employee == null) {
            System.out.println("  No employee profile linked to your account.");
        }
        return employee;
    }

    /**
     * Get employees by department name.
     *
     * @param department - department to filter by
     * @return List of employees in that department
     */
    public List<Employee> getByDepartment(String department) {
        List<Employee> employees = employeeDAO.getEmployeesByDepartment(department);
        if (employees == null || employees.isEmpty()) {
            System.out.println("  No employees found in department: " + department);
        }
        return employees;
    }

    /**
     * Update an existing employee's details.
     * Fetches the current record, applies changes, then saves.
     *
     * @param id         - ID of employee to update
     * @param name       - new name (pass null to keep existing)
     * @param email      - new email (pass null to keep existing)
     * @param department - new department (pass null to keep existing)
     * @param salary     - new salary (-1 to keep existing)
     * @param phone      - new phone (pass null to keep existing)
     * @return true if updated successfully
     */
    public boolean updateEmployee(int id, String name, String email,
                                  String department, double salary, String phone) {
        // First, fetch the existing employee record
        Employee employee = employeeDAO.getEmployeeById(id);
        if (employee == null) {
            System.out.println("  No employee found with ID: " + id);
            return false;
        }

        // Update only the fields that were provided (not null / not -1)
        if (name != null && !name.trim().isEmpty())       employee.setName(name);
        if (email != null && !email.trim().isEmpty())     employee.setEmail(email);
        if (department != null && !department.trim().isEmpty()) employee.setDepartment(department);
        if (salary > 0)                                   employee.setSalary(salary);
        if (phone != null && !phone.trim().isEmpty())     employee.setPhone(phone);

        // Save the updated record
        boolean updated = employeeDAO.updateEmployee(employee);
        if (updated) {
            System.out.println("  Employee updated successfully!");
        } else {
            System.out.println("  Failed to update employee.");
        }
        return updated;
    }

    /**
     * Delete an employee by ID.
     *
     * @param id - ID of the employee to delete
     * @return true if deleted successfully
     */
    public boolean deleteEmployee(int id) {
        boolean deleted = employeeDAO.deleteEmployee(id);
        if (deleted) {
            System.out.println("  Employee with ID " + id + " deleted successfully.");
        }
        return deleted;
    }
}
