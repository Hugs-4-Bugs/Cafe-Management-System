Cafe Management System

## Overview
Hugs-4-Bugs Cafe Management System is a **Spring Boot-based** application designed to streamline and automate cafe management processes. It provides functionalities for managing users, products, categories, bills, and authentication.

## Features
- **User Management**: Admins can manage user accounts and roles.
- **Category Management**: CRUD operations on product categories.
- **Product Management**: Manage cafe products with features like add, update, delete, and view.
- **Billing System**: Generate and manage bills for orders.
- **Authentication & Security**: JWT-based authentication and role-based access control.
- **Dashboard**: Overview of orders, revenue, and user activities.
- **Email Notification**: Email functionality integrated for communication.

## Technologies Used
- **Backend**: Java, Spring Boot, Spring Security, Hibernate, JWT
- **Database**: MySQL
- **Build Tool**: Maven
- **Containerization**: Docker
- **API Testing**: Postman
- **Version Control**: GitHub

## Installation & Setup
### **Prerequisites**
- Java 17+
- Maven 3+
- MySQL 8+
- Docker (optional)
- Postman (for API testing)

### **Clone the Repository**
```bash
 git clone https://github.com/Hugs-4-Bugs/Cafe-Management-System.git
 cd Cafe-Management-System
```

### **Database Setup**
1. Create a new MySQL database:
   ```sql
   CREATE DATABASE cafe_management;
   ```
2. Update `application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cafe_management
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

### **Build and Run the Application**
#### **Using Maven**
```bash
mvn clean install
mvn spring-boot:run
```
#### **Using Java Command**
```bash
mvn package
java -jar target/hugs-4-bugs-cafe-management-system.jar
```

#### **Using Docker**
```bash
docker build -t cafe-management .
docker run -p 8080:8080 cafe-management
```

### **API Endpoints**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/user/login` | POST | User Login |
| `/api/user/register` | POST | Register User |
| `/api/category/add` | POST | Add Category |
| `/api/product/add` | POST | Add Product |
| `/api/product/delete/{id}` | DELETE | Delete Product |
| `/api/bill/generate` | POST | Generate Bill |
| `/api/dashboard/stats` | GET | Dashboard Stats |

## **Project Directory Structure**
```
├── hugs-4-bugs-cafe-management-system/
│   ├── README.md
│   ├── cafe-Dependency.txt
│   ├── Dockerfile
│   ├── HELP.md
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── Bill Document/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/inn/cafe/
│   │   │   │   ├── CafeManagementSystemApplication.java
│   │   │   │   ├── Constents/
│   │   │   │   ├── DAO/
│   │   │   │   ├── JWT/
│   │   │   │   ├── POJO/
│   │   │   │   ├── Rest/
│   │   │   │   ├── RestImpl/
│   │   │   │   ├── Service/
│   │   │   │   ├── ServiceImpl/
│   │   │   │   ├── Utils/
│   │   │   │   └── Wrapper/
│   │   ├── resources/application.properties
│   └── test/java/com/inn/cafe/CafeManagementSystemApplicationTests.java
│   └── .github/workflows/maven-publish.yml
```

## **Contribution**
Contributions are welcome! Fork the repository, make changes, and submit a pull request.

## **License**
This project is licensed under the MIT License.

## **Contact**
For any queries, reach out at **your-email@example.com** or open an issue on GitHub.

