# FreshMart — Component 1: User Authentication & Account Management

This ZIP contains only **Component 1** separated from the original FreshMart grocery website.

## Included Features

- User registration
- User login
- User logout
- Session-based authentication
- Profile view and update
- Admin user management
- File-based user storage using `data/users.txt`

## Removed From This Component

The following original website components were intentionally removed from this ZIP:

1. Product Management
2. Shopping Cart
3. Order Management
4. Payment & Checkout
5. Admin Dashboard modules unrelated to users

## Project Structure

```text
FreshMart-Component-1-User-Authentication/
├── pom.xml
├── mvnw / mvnw.cmd
├── data/
│   └── users.txt
└── src/main/
    ├── java/com/grocery/
    │   ├── GroceryApplication.java
    │   ├── DataInitializer.java
    │   ├── controller/AuthController.java
    │   ├── model/BaseEntity.java
    │   ├── model/Searchable.java
    │   ├── model/User.java
    │   ├── service/UserService.java
    │   └── util/FileUtil.java
    └── resources/
        ├── application.properties
        ├── data/users.txt
        ├── static/css/style.css
        ├── static/js/main.js
        └── templates/
            ├── index.html
            └── auth/
                ├── login.html
                ├── register.html
                ├── profile.html
                └── admin-users.html
```

## How to Run

### Requirements

- Java 17+
- Maven installed, or IntelliJ IDEA with Maven support

### Run with Maven

```bash
mvn spring-boot:run
```

You can also use the included `mvnw` / `mvnw.cmd` helper scripts if Maven is installed on your PATH:

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Demo Login Credentials

| Role | Email | Password |
|---|---|---|
| Admin | admin@freshmart.com | admin123 |
| Customer | john@email.com | pass123 |
| Customer | mary@email.com | pass123 |

## Data Format

`users.txt`

```text
id|name|email|password|phone|role|createdAt
```

Example:

```text
1|Admin User|admin@freshmart.com|admin123|+1-555-000-0001|ADMIN|2025-01-01 09:00
```

## Component Mapping

This component corresponds to:

> **User Authentication**  
> Register, Login, Logout, Profile Edit, Admin User List
