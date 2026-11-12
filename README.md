Mini IAM & Policy Engine

A Java Spring Boot-based Identity and Access Management (IAM) system that simulates cloud access control using Role-Based Access Control (RBAC), Attribute-Based Access Control (ABAC), JWT authentication, and audit logging.

Project Overview

Mini IAM & Policy Engine is designed to manage users, roles, permissions, and access to simulated cloud resources. It evaluates access requests based on assigned permissions and configured policies.

Features

- User Management: Create and manage users.
- Role-Based Access Control (RBAC): ADMIN, DEVELOPER, and VIEWER roles.
- Permission Management: Assign permissions to roles.
- JWT Authentication: Secure login and authenticated API access.
- Policy Engine: Evaluates access requests before allowing operations.
- Time-Based Access Control: Restricts policy-protected actions to working hours (9 AM–5 PM).
- Department-Based Access Control (ABAC): Supports department-based policy checks.
- Cloud Resource Simulation: Manage virtual machines, storage, and databases.
- Audit Logging: Records allowed and denied access decisions.
- Web Dashboard: HTML, CSS, and JavaScript interface for interacting with the system.

Technology Stack

Component| Technology
Backend| Java 21, Spring Boot
Database| MySQL
ORM| Spring Data JPA / Hibernate
Security| Spring Security, JWT, BCrypt
Frontend| HTML, CSS, JavaScript
Build Tool| Maven
IDE| Visual Studio Code

User Roles

Role| Description
ADMIN| Full administrative permissions
DEVELOPER| Access to development-related operations
VIEWER| Read-only access to permitted resources

Project Structure

mini-iam/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/iam/mini_iam/
│       │       ├── controller/
│       │       ├── entity/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   ├── js/
│           │   ├── index.html
│           │   └── dashboard.html
│           └── application.properties
├── pom.xml
├── mvnw
└── mvnw.cmd

Setup and Installation

Prerequisites

- Java 21
- MySQL
- Git

Steps

1. Clone the repository:

git clone : https://github.com/maheenkhan-lang/Mini-IAM-Policy-Engine.git

2. Open the project folder.

3. Configure your MySQL database and environment variables:

DB_USERNAME
DB_PASSWORD
JWT_SECRET

4. Start the application using Maven:

.\mvnw.cmd spring-boot:run

5. Open the application in your browser:

http://localhost:8080

Security Note

Database credentials, JWT secrets, and user passwords should be configured through environment variables and must not be committed to the repository.

Project Purpose

Developed as a Cloud Computing academic project to demonstrate identity management, authentication, authorization, policy evaluation, and cloud resource access control.