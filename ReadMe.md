0d1 Clinic Management System 3e5 4bb

This project is a **Spring Boot microservices-based backend** for a Clinic Management System. It demonstrates service discovery, independent services, database isolation, inter-service communication, and API Gateway routing.

---

## 4c1 Monorepo Structure

This repository contains the following microservices:

- **eureka-server**: Service Registry (Port: 8761)
- **doctor-service**: Manages doctors (Port: 8081)
- **patient-service**: Manages patients (Port: 8082)
- **appointment-service**: Manages appointments (Port: 8083)
- **notification-service**: Sends notifications (Port: 8084)
- **api-gateway**: API Gateway for routing and security (Port: 8080)

Each service is a standalone Spring Boot project with its own Maven build and database (where applicable).

---

## 527 Tech Stack

- Java 21
- Spring Boot 3.2.x
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Data JPA
- MySQL
- Maven
- Postman

---

## 4da Service Overview

### 1. Eureka Server
- **Purpose:** Central registry for all microservices (service discovery)
- **Port:** 8761
- **Dashboard:** http://localhost:8761

### 2. Doctor Service
- **Purpose:** Manages doctor information
- **Port:** 8081
- **Database:** MySQL (doctor)
- **Endpoints:** `/doctors`, `/doctors/{id}` (CRUD)

### 3. Patient Service
- **Purpose:** Manages patient information
- **Port:** 8082
- **Database:** MySQL (patient)
- **Endpoints:** `/patients`, `/patients/{id}` (CRUD)

### 4. Appointment Service
- **Purpose:** Manages appointments, coordinates between Doctor and Patient services
- **Port:** 8083
- **Database:** MySQL (appointment)
- **Endpoints:** `/appointments`, `/appointments/{id}` (CRUD)

### 5. Notification Service
- **Purpose:** Sends notifications (e.g., appointment reminders)
- **Port:** 8084

### 6. API Gateway
- **Purpose:** Single entry point for all APIs, routing, and security
- **Port:** 8080
- **Routes:** `/doctors/**`, `/patients/**`, `/appointments/**` (see `api-gateway/src/main/resources/application.yml`)

---

## 4e6 Build & Run Instructions

**Prerequisites:**
- Java 21
- Maven
- MySQL running with databases for each service (doctor, patient, appointment)

**Build All Services:**

You must build each service separately. Open a terminal in the root of each service folder and run:

```sh
mvn clean install
```

**Order to Build & Run:**

1. **Eureka Server**
    - `cd eureka-server`
    - `mvn spring-boot:run`
2. **Doctor Service**
    - `cd doctor-service`
    - `mvn spring-boot:run`
3. **Patient Service**
    - `cd patient-service`
    - `mvn spring-boot:run`
4. **Appointment Service**
    - `cd appointment-service`
    - `mvn spring-boot:run`
5. **Notification Service**
    - `cd notification-service`
    - `mvn spring-boot:run`
6. **API Gateway**
    - `cd api-gateway`
    - `mvn spring-boot:run`

**Note:**
- Wait for each service to start before launching the next.
- All services must register with Eureka before the API Gateway can route requests.

---

## 50e Testing

- Use the provided Postman collections:
  - `Clinic System - API Gateway (8080).postman_collection.json`
  - `Clinic System - CRUD (Direct Services).postman_collection.json`
- Each service exposes REST endpoints for full CRUD operations.
- You can test each service independently or via the API Gateway.

---

## 4a1 Microservices Principles Demonstrated

- Service Discovery (Eureka)
- Independent microservices
- Database per service
- REST-based inter-service communication
- API Gateway pattern
- Scalable, modular backend architecture

---

## 680 Future Enhancements

- Frontend (React / Angular)
- Security (JWT Authentication)
- CORS configuration
- Centralized logging & monitoring

---

## 464 Author

Pranshu Saraswat  
BE Computer Science  
Clinic Management System – Microservices Project