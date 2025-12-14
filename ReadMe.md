# 🏥 Clinic Management System – Microservices Project

This project is a **complete full-stack Clinic Management System** built with Spring Boot microservices backend and a modern HTML/CSS/JavaScript frontend. It demonstrates service discovery, independent services, database isolation, inter-service communication, and API Gateway routing.

## ✨ What's New
- 🎨 **Modern Web Frontend** - Complete HTML/CSS/JavaScript interface
- 🔄 **Full CRUD Operations** - Manage doctors, patients, appointments, and billing
- 📊 **Real-time Dashboard** - Live statistics and monitoring
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile
- 🚀 **Quick Start Scripts** - PowerShell and Batch scripts to start all services

---

## 🏗️ Monorepo Structure

This repository contains the following microservices:

- **eureka-server**: Service Registry (Port: 8761)
- **doctor-service**: Manages doctors (Port: 8081)
- **patient-service**: Manages patients (Port: 8082)
- **appointment-service**: Manages appointments (Port: 8083)
- **billing-service**: Manages billing (Port: 8084)
- **notification-service**: Sends email notifications (Port: 8085)
- **api-gateway**: API Gateway for routing (Port: 8080)
- **frontend**: Modern web interface (HTML/CSS/JavaScript)

Each service is a standalone Spring Boot project with its own Maven build and database (where applicable).

---

## 🛠️ Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.x
- Spring Cloud Netflix Eureka (Service Discovery)
- Spring Cloud Gateway (API Gateway)
- Spring Data JPA
- MySQL / H2 Database
- Maven
- JavaMailSender (Email notifications)

### Frontend
- HTML5
- CSS3 (Modern, responsive design)
- Vanilla JavaScript (ES6+)
- Fetch API for RESTful communication

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

### 4. Appointment Service (Orchestrator)
- **Purpose:** Manages appointments, coordinates between Doctor, Patient, Billing, and Notification services
- **Port:** 8083
- **Database:** MySQL (appointment_db)
- **Endpoints:** 
  - `GET /appointments` - List all appointments
  - `POST /appointments` - Book appointment (triggers billing & notification)
  - `GET /appointments/{id}` - Get appointment by ID
  - `DELETE /appointments/{id}` - Cancel appointment

### 5. Billing Service
- **Purpose:** Manages financial records and billing
- **Port:** 8084
- **Database:** MySQL (billing_db)
- **Endpoints:**
  - `POST /bills?appointmentId={id}&patientId={id}` - Create bill
  - `GET /bills/{id}` - Get bill by ID
  - `GET /bills/patient/{patientId}` - Get all bills for a patient
  - `PUT /bills/{id}/pay` - Mark bill as paid
  - `DELETE /bills/{id}` - Delete bill

### 6. Notification Service
- **Purpose:** Sends email notifications for appointments
- **Port:** 8085
- **Features:** JavaMailSender integration, SMTP configuration

### 7. API Gateway
- **Purpose:** Single entry point for all APIs, routing requests to appropriate services
- **Port:** 8080
- **Routes:** 
  - `/doctors/**` → Doctor Service
  - `/patients/**` → Patient Service
  - `/appointments/**` → Appointment Service
  - `/bills/**` → Billing Service

### 8. Frontend (NEW! 🎉)
- **Purpose:** Complete web interface for all operations
- **Technology:** HTML5, CSS3, Vanilla JavaScript
- **Features:**
  - Doctor management (Create, Read, Update, Delete)
  - Patient management (Create, Read, Update, Delete)
  - Appointment booking with date picker
  - Billing management with search by patient
  - Real-time dashboard with statistics
  - Toast notifications for user feedback
  - Loading overlays for better UX
  - Fully responsive design

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