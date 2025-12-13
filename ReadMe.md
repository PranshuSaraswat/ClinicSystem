🏥 Clinic Management System – Microservices Backend

This project is a Spring Boot microservices-based backend for a Clinic Management System.
It demonstrates service discovery, independent services, database isolation, and inter-service communication.

⚠️ This README covers all backend services EXCEPT the API Gateway.

🧩 Architecture Overview

The system consists of 4 backend components:

Eureka Server – Service Registry

Doctor Service – Manages doctors

Patient Service – Manages patients

Appointment Service – Manages appointments and coordinates between doctors and patients

Each service:

Runs independently

Has its own database

Registers with Eureka

Communicates via REST APIs

🛠 Tech Stack

Java 21

Spring Boot 3.2.x

Spring Cloud Netflix Eureka

Spring Data JPA

MySQL

Maven

Postman

📌 Services Description
1️⃣ Eureka Server (Service Registry)

Purpose

Central registry where all microservices register themselves

Enables service discovery without hardcoded URLs

Port
8761

Access Dashboard
http://localhost:8761

2️⃣ Doctor Service

Purpose

Manages doctor information

Port
8081

Database

MySQL (doctor database)

Endpoints

POST /doctors

GET /doctors

GET /doctors/{id}

PUT /doctors/{id}

DELETE /doctors/{id}

Sample JSON

{
  "name": "Dr. Sharma",
  "specialization": "Cardiology",
  "available": true
}

3️⃣ Patient Service

Purpose

Manages patient information

Port
8082

Database

MySQL (patient database)

Endpoints

POST /patients

GET /patients

GET /patients/{id}

PUT /patients/{id}

DELETE /patients/{id}

Sample JSON

{
  "name": "Amit Verma",
  "age": 30,
  "gender": "Male"
}

4️⃣ Appointment Service

Purpose

Manages appointments

Coordinates between Doctor and Patient services

Port
8083

Database

MySQL (appointment database)

Inter-Service Communication

Uses RestTemplate to call:

Doctor Service

Patient Service

Endpoints

POST /appointments

GET /appointments

GET /appointments/{id}

DELETE /appointments/{id}

Sample JSON

{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDate": "2025-12-20"
}

🔁 Microservices Communication Rules

Services do NOT share databases

Services do NOT share entities

Communication happens ONLY via REST APIs

Appointment Service acts as the coordinator

This follows proper microservices architecture principles.

▶️ How to Run the System
Step 1: Start Eureka Server
cd eureka-server
mvn spring-boot:run

Step 2: Start Doctor Service
cd doctor-service
mvn spring-boot:run

Step 3: Start Patient Service
cd patient-service
mvn spring-boot:run

Step 4: Start Appointment Service
cd appointment-service
mvn spring-boot:run

🧪 Testing

APIs are tested using Postman

Each service can be tested independently

Full CRUD operations are supported

🎯 Key Concepts Demonstrated

Service Discovery using Eureka

Independent microservices

Database per service

REST-based inter-service communication

Scalable backend architecture

🚀 Future Enhancements

API Gateway

Frontend (React / Angular)

Security (JWT Authentication)

CORS configuration

Centralized logging

👨‍💻 Author

Pranshu Saraswat
BE Computer Science
Clinic Management System – Microservices Project