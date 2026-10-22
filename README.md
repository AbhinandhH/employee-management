# Employee Management System

## 1. Overview

The Employee Management System is a RESTful API developed using Java and Spring Boot. It allows users to create, retrieve, update, delete, and search employee records.

The application supports request validation, centralized exception handling, pagination, sorting, and dynamic filtering based on employee details.

## 2. Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- Oracle Database / MySQL
- Maven
- MapStruct
- Lombok
- Jakarta Bean Validation
- Springdoc OpenAPI / Swagger UI

## 3. Features

- Create a new employee
- Retrieve an employee by ID
- Retrieve all employees
- Update employee details
- Delete an employee
- Search employees by name
- Filter employees by department and status
- Filter employees by minimum and maximum salary
- Pagination and sorting
- Request validation
- Centralized exception handling
- API documentation using Swagger UI

## 4. Prerequisites

Install the following before running the application:

- Java JDK compatible with the project
- Maven or the Maven Wrapper included in the repository
- Oracle Database or MySQL, depending on the configured database

## 5. Setup Instructions

### Step 1: Clone the repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd <YOUR_PROJECT_DIRECTORY>
```

### Step 2: Configure the database

Update `src/main/resources/application.properties` with your database connection details.

Example for MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_test_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

Use the appropriate JDBC URL and driver for Oracle if your project uses Oracle Database.

Set the `DB_USERNAME` and `DB_PASSWORD` environment variables before starting the application.

**Note:** The configuration above is an example. Use the actual database settings and schema-management strategy configured in your project.

### Step 3: Run the application

Using the Maven Wrapper on macOS or Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The application usually starts at:

`http://localhost:8080`

## 6. API Endpoints

Base URL: `http://localhost:8080/api/employee`

| HTTP Method | Endpoint | Description | Success Status |
|---|---|---|---|
| POST | `/save` | Create an employee | 201 Created |
| GET | `/{id}` | Retrieve an employee by ID | 200 OK |
| PUT | `/update/{id}` | Update an employee | 200 OK |
| GET | `/list` | Retrieve all employees | 200 OK |
| GET | `/pageablelist` | Retrieve employees with filtering, pagination, and sorting | 200 OK |
| DELETE | `/{id}` | Delete an employee | 200 OK |

## 7. Sample API Requests

### 7.1 Create an Employee

**Request**

```http
POST http://localhost:8080/api/employee/save
Content-Type: application/json
```

**Request Body**

```json
{
  "name": "Abhinandh H",
  "email": "abhinandhh@example.com",
  "department": "IT",
  "phoneNumber": "9876543210",
  "salary": 45000.00,
  "age": 25,
  "status": "ACTIVE"
}
```

**Sample Response — 201 Created**

```json
{
  "id": 1,
  "name": "Abhinandh H",
  "email": "abhinandhh@example.com",
  "department": "IT",
  "phoneNumber": "9876543210",
  "salary": 45000.00,
  "age": 25,
  "status": "ACTIVE"
}
```

The request and response fields above are examples; adjust them to match your actual DTOs.

### 7.2 Retrieve an Employee by ID

**Request**

```http
GET http://localhost:8080/api/employee/1
```

Returns the employee details for the specified ID.

### 7.3 Retrieve All Employees

**Request**

```http
GET http://localhost:8080/api/employee/list
```

Returns a list of employee records.

### 7.4 Update an Employee

**Request**

```http
PUT http://localhost:8080/api/employee/update/1
Content-Type: application/json
```

**Request Body**

```json
{
  "name": "Abhiandh H",
  "email": "abhinandhh@example.com",
  "department": "IT",
  "phoneNumber": "9876543210",
  "salary": 50000.00,
  "age": 26,
  "status": "ACTIVE"
}
```

Returns the updated employee details.

### 7.5 Delete an Employee

**Request**

```http
DELETE http://localhost:8080/api/employee/delete/1
```

Deletes the employee with the specified ID and returns the deleted employee response, according to the current controller implementation.

## 8. Filtering, Pagination and Sorting

The `/pageablelist` endpoint supports the following optional query parameters:

| Parameter | Description | Example |
|---|---|---|
| `name` | Search by employee name | `name=Abhi` |
| `department` | Filter by department | `department=IT` |
| `minSalary` | Minimum salary | `minSalary=30000` |
| `maxSalary` | Maximum salary | `maxSalary=60000` |
| `status` | Filter by employee status | `status=ACTIVE` |
| `page` | Zero-based page number | `page=0` |
| `size` | Number of records per page | `size=5` |
| `sort` | Property and sort direction | `sort=name,asc` |

### Example: Filter employees

```http
GET http://localhost:8080/api/employee/pageablelist?department=IT&status=ACTIVE
```

### Example: Filter by salary range

```http
GET http://localhost:8080/api/employee/pageablelist?minSalary=30000&maxSalary=60000
```

### Example: Search with pagination and sorting

```http
GET http://localhost:8080/api/employee/pageablelist?name=john&status=ACTIVE&page=0&size=5&sort=name,asc
```

The endpoint returns a Spring Data `Page` response containing employee records and pagination metadata, such as the current page, total elements, and total pages.

Page numbering starts at `0`. The default page size is `10`, and the default sort is by `id` in ascending order.

Use the exact enum values defined in the `Status` enum, such as `ACTIVE`, if those are the actual constants.

## 9. Validation and Error Handling

Employee creation and update requests use `@Valid` to trigger request validation.

The application uses centralized exception handling, if configured, to provide consistent error responses.

Common HTTP status codes include:

- `200 OK`: Request completed successfully.
- `201 Created`: Employee created successfully.
- `400 Bad Request`: Invalid request data or validation failure.
- `404 Not Found`: Employee not found, if handled accordingly.
- `500 Internal Server Error`: Unexpected server-side error.

The exact error response structure depends on the exception-handling implementation.

## 10. Swagger API Documentation

If Springdoc OpenAPI is configured, start the application and open:

Swagger UI:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI specification:

`http://localhost:8080/v3/api-docs`

Swagger UI allows you to view the API endpoints and execute requests directly from the browser.

## 11. Running Tests

Run the project tests using:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## 12. Project Structure

```text
src/main/java/com/assessment/employeemanagement/
├── controller/
├── service/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── repository/
├── mapper/
├── specification/
├── enums/
├── exception/
└── config/
```

The package structure above is illustrative. Adjust it to reflect the actual directories in your repository.

## 13. Author

[Abhinandh H]
