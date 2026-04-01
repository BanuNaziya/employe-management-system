# Employee Management System

A console-based backend application built with **Java**, **Hibernate**, **JDBC**, and **MySQL**.

Supports full **CRUD operations**, **user authentication**, and **Role-Based Access Control (RBAC)**.

---

## Features

- **Authentication** — Secure login with username and password
- **RBAC (Role-Based Access Control)**
  - `ADMIN` — Full access: add, view, update, delete employees; create user accounts
  - `EMPLOYEE` — Restricted access: view own profile only
- **Employee CRUD** — Create, Read, Update, Delete employee records
- **Search** — Find employees by ID or department
- **Hibernate ORM** — Java objects mapped to MySQL tables automatically
- **JDBC** — MySQL connectivity via `mysql-connector-j`

---

## Tech Stack

| Technology  | Purpose                            |
|-------------|------------------------------------|
| Java 11     | Core programming language          |
| Hibernate 5 | ORM — maps Java classes to DB tables |
| JDBC        | Database connectivity (via Hibernate) |
| MySQL 8     | Relational database                |
| Maven       | Build and dependency management    |

---

## Project Structure

```
employeManagmentSystem/
├── src/
│   └── main/
│       ├── java/com/ems/
│       │   ├── Main.java                  # Entry point, menus
│       │   ├── config/
│       │   │   └── HibernateUtil.java     # Hibernate SessionFactory setup
│       │   ├── model/
│       │   │   ├── User.java              # User entity (login accounts)
│       │   │   └── Employee.java          # Employee entity (records)
│       │   ├── dao/
│       │   │   ├── UserDAO.java           # DB operations for users
│       │   │   └── EmployeeDAO.java       # DB operations for employees (CRUD)
│       │   └── service/
│       │       ├── AuthService.java       # Login and registration logic
│       │       └── EmployeeService.java   # Employee business logic
│       └── resources/
│           └── hibernate.cfg.xml          # DB connection configuration
├── database/
│   └── setup.sql                          # SQL script to create DB and tables
├── pom.xml                                # Maven dependencies
└── README.md
```

---

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+

---

## Setup Instructions

### Step 1 — Clone the repository

```bash
git clone https://github.com/BanuNaziya/employe-management-system.git
cd employe-management-system
```

### Step 2 — Create the MySQL database

Open MySQL and run:

```bash
mysql -u root -p < database/setup.sql
```

Or copy-paste the contents of `database/setup.sql` into MySQL Workbench / DBeaver.

### Step 3 — Update database credentials

Open `src/main/resources/hibernate.cfg.xml` and update:

```xml
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">your_password_here</property>
```

### Step 4 — Build the project

```bash
mvn clean package
```

### Step 5 — Run the application

**Option A — using Maven:**
```bash
mvn exec:java
```

**Option B — using the packaged jar:**
```bash
java -jar target/employee-management-system-1.0-jar-with-dependencies.jar
```

---

## Default Login

On first run, the system creates a default admin account automatically:

| Username | Password | Role  |
|----------|----------|-------|
| `admin`  | `admin123` | ADMIN |

> **Note:** Change the password after first login.

---

## How It Works

### Login Flow
```
Start App
   ↓
Login Screen (username + password)
   ↓
Verify in DB → Match? Yes → Check Role
                         No  → Show error, retry
   ↓
ADMIN Role   → Admin Menu (Full CRUD)
EMPLOYEE Role → Employee Menu (View profile only)
```

### RBAC
| Feature                  | ADMIN | EMPLOYEE |
|--------------------------|-------|----------|
| Add Employee             | ✅    | ❌       |
| View All Employees       | ✅    | ❌       |
| Search by ID             | ✅    | ❌       |
| Search by Department     | ✅    | ❌       |
| Update Employee          | ✅    | ❌       |
| Delete Employee          | ✅    | ❌       |
| Create User Account      | ✅    | ❌       |
| View Own Profile         | ✅    | ✅       |

---

## Database Schema

### users
| Column   | Type         | Description          |
|----------|--------------|----------------------|
| id       | INT (PK)     | Auto-increment ID    |
| username | VARCHAR(50)  | Unique login name    |
| password | VARCHAR(100) | Login password       |
| role     | VARCHAR(20)  | ADMIN or EMPLOYEE    |

### employees
| Column     | Type         | Description               |
|------------|--------------|---------------------------|
| id         | INT (PK)     | Auto-increment ID         |
| name       | VARCHAR(100) | Full name                 |
| email      | VARCHAR(100) | Unique email address      |
| department | VARCHAR(50)  | Department name           |
| salary     | DOUBLE       | Salary amount             |
| phone      | VARCHAR(15)  | Contact number            |
| user_id    | INT (FK)     | Linked user account (optional) |

---

## Author

**Banu Naziya**  
Java Backend Developer  
Skills: Java, Hibernate, JDBC, MySQL
