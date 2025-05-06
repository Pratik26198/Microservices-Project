# Microservices-Project
# Task Management System

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A robust microservices project for task management with JWT authentication, role-based access control, and GitHub submission tracking. Runs on port `5000`.

## 📋 Features
- **JWT Authentication** with role-based authorization
- **CRUD Operations** for tasks and submissions
- Task assignment and status tracking (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
- GitHub link submissions with acceptance workflow
- User profile management

## 🔑 Authentication Flow
1. Signup at `/auth/signup` to get JWT
2. Include JWT in `Authorization: Bearer {token}` header for secured endpoints
3. Roles: `USER` (default), `ADMIN`

## 🚀 API Endpoints

### Authentication (`POST /auth`)
| Endpoint    | Description          | Request Body Example                          |
|-------------|----------------------|-----------------------------------------------|
| `/signup`   | Register new user    | `{email, password, fullName, role?}`          |
| `/signin`   | Login & get JWT      | `{email: "user@domain.com", password: "***"}` |

### Users (`GET /api/users`)
| Endpoint     | Description              | Access  |
|--------------|--------------------------|---------|
| `/profile`   | Get current user details | All     |
| `/`          | List all users           | ADMIN   |

### Tasks (`/api/tasks`)
| Method | Endpoint                         | Description                               |
|--------|----------------------------------|-------------------------------------------|
| POST   | `/`                              | Create new task (Admin-only)              |
| GET    | `/{id}`                          | Get task by ID                            |
| GET    | `/user?status={status}`          | Get assigned tasks with optional filter   |
| GET    | `/?status={status}`              | Get all tasks (Admin)                     |
| PUT    | `/{id}/user/{userId}/assigned`   | Assign task to user                       |
| PUT    | `/{id}`                          | Update task details                       |
| PUT    | `/{id}/complete`                 | Mark task as completed                    |
| DELETE | `/{id}`                          | Delete task (Admin-only)                  |

### Submissions (`/api/submissions`)
| Method | Endpoint                | Description                               |
|--------|-------------------------|-------------------------------------------|
| POST   | `/?task_id=&github_link=`| Submit task with GitHub link              |
| GET    | `/{id}`                 | Get submission by ID                      |
| GET    | `/`                     | List all submissions (Admin)              |
| GET    | `/task/{taskId}`        | Get submissions for specific task         |
| PUT    | `/{id}?status=`         | Accept/decline submission (`ACCEPTED/DECLINED`) |

## 🛠️ Example Requests

### **Create User** (Signup)

```bash
curl -X POST http://localhost:5000/auth/signup \
-H "Content-Type: application/json" \
-d '{
  "email": "dev@taskify.com",
  "password": "secure123",
  "fullName": "John Developer",
  "role": "ADMIN"
}'
```

---

### 📤 Submit Task

```bash
curl -X POST http://localhost:5000/api/submissions \
-H "Authorization: Bearer eyJhb..." \
-F "task_id=5" \
-F "github_link=https://github.com/user/task-submission"
```

---

### 👤 Assign Task

```bash
curl -X PUT http://localhost:5000/api/tasks/15/user/8/assigned \
-H "Authorization: Bearer eyJhb..."
```

---

## ⚙️ Project Setup

### Requirements

- Java 17  
- Maven  
- MySQL/PostgreSQL  

---

### Configuration

Create `application.properties`:

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/databaseName
spring.datasource.username=enterUserName
spring.datasource.password=enterPassword
jwt.secret=enter-512-bit-secret-key
```

---

### Run Application

```bash
mvn clean install
mvn spring-boot:run -Dserver.port=5000
```

---

## 📦 Dependencies

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
</dependencies>
```

---

## 📝 Key Implementation Details

### 🔐 Security Configuration

- JWT validation in `UserService.getUserProfile()`
- Role checks in `TaskService.createTask()`

---

### 🔄 State Management

- Task status transitions enforced in `completeTask()`
- Submission lifecycle handled in `acceptDeclineSubmission()`

---

### ✅ Validation

- Email uniqueness check during signup
- Task ownership validation in `updateTask()`

