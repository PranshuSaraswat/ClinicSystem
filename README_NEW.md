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

## 🎯 Service Overview

### 1. Eureka Server
- **Purpose:** Central registry for all microservices (service discovery)
- **Port:** 8761
- **Dashboard:** http://localhost:8761

### 2. Doctor Service
- **Purpose:** Manages doctor information (CRUD operations)
- **Port:** 8081
- **Database:** MySQL (doctor_db)
- **Endpoints:** 
  - `GET /doctors` - List all doctors
  - `POST /doctors` - Create doctor
  - `GET /doctors/{id}` - Get doctor by ID
  - `PUT /doctors/{id}` - Update doctor
  - `DELETE /doctors/{id}` - Delete doctor

### 3. Patient Service
- **Purpose:** Manages patient information (CRUD operations)
- **Port:** 8082
- **Database:** MySQL (patient_db)
- **Endpoints:** 
  - `GET /patients` - List all patients
  - `POST /patients` - Create patient
  - `GET /patients/{id}` - Get patient by ID
  - `PUT /patients/{id}` - Update patient
  - `DELETE /patients/{id}` - Delete patient

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

## 🚀 Quick Start

### Option 1: Automated Startup (Recommended)

**Using PowerShell:**
```powershell
.\start-all-services.ps1
```

**Using Batch File:**
```batch
start-all-services.bat
```

These scripts will:
- Start all 7 services in the correct order
- Wait appropriate times between starts
- Open Eureka Dashboard automatically
- Display helpful status information

### Option 2: Manual Startup

**Prerequisites:**
- Java 21
- Maven
- MySQL (or H2 for in-memory database)

**Order to Start Services:**

1. **Eureka Server**
   ```sh
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Doctor Service**
   ```sh
   cd doctor-service
   mvn spring-boot:run
   ```

3. **Patient Service**
   ```sh
   cd patient-service
   mvn spring-boot:run
   ```

4. **Appointment Service**
   ```sh
   cd appointment-service
   mvn spring-boot:run
   ```

5. **Billing Service**
   ```sh
   cd billing-service
   mvn spring-boot:run
   ```

6. **Notification Service**
   ```sh
   cd notification-service
   mvn spring-boot:run
   ```

7. **API Gateway**
   ```sh
   cd api-gateway
   mvn spring-boot:run
   ```

### Starting the Frontend

After all backend services are running:

1. Navigate to the frontend folder
2. Open `index.html` in your web browser
3. Or use a local server:
   ```bash
   cd frontend
   python -m http.server 8000
   # Then open http://localhost:8000
   ```

**Note:**
- Wait for each service to fully start before launching the next
- All services must register with Eureka before testing
- Verify all services are UP at http://localhost:8761

---

## 📚 Documentation

- **[Frontend README](frontend/README.md)** - Detailed frontend guide
- **[Setup Guide](SETUP_GUIDE.md)** - Complete setup and troubleshooting
- **Postman Collections** - API testing collections included

---

## 🧪 Testing

### Using the Web Frontend
1. Open `frontend/index.html` in your browser
2. Navigate through different sections:
   - **Dashboard** - View system statistics
   - **Doctors** - Manage doctor records
   - **Patients** - Manage patient records
   - **Appointments** - Book and manage appointments
   - **Billing** - Create and manage bills

### Using Postman
- `Clinic System - API Gateway (8080).postman_collection.json`
- `Clinic System - CRUD (Direct Services).postman_collection.json`

### Direct API Testing
Each service exposes REST endpoints accessible via the API Gateway (port 8080) or directly.

---

## 🏗️ Microservices Principles Demonstrated

- ✅ Service Discovery (Eureka)
- ✅ Independent microservices
- ✅ Database per service
- ✅ REST-based inter-service communication
- ✅ API Gateway pattern
- ✅ Service orchestration (Appointment Service)
- ✅ Event-driven notifications
- ✅ Scalable, modular architecture
- ✅ CORS-enabled API Gateway
- ✅ Full-stack integration (Backend + Frontend)

---

## 🎯 Features Implemented

### Backend
- ✅ Complete CRUD operations for all entities
- ✅ Service-to-service communication using RestTemplate
- ✅ Automatic bill creation on appointment booking
- ✅ Email notifications for appointments
- ✅ Centralized routing through API Gateway
- ✅ Service discovery and registration

### Frontend
- ✅ Modern, responsive UI design
- ✅ Real-time dashboard with statistics
- ✅ Form validation and error handling
- ✅ Toast notifications for user feedback
- ✅ Loading overlays during API calls
- ✅ Dynamic data tables
- ✅ Search and filter functionality
- ✅ Mobile-friendly responsive design

---

## 🚀 Future Enhancements

- 🔐 Security (JWT Authentication & Authorization)
- 📊 Advanced analytics and reporting
- 🔄 Message queue integration (RabbitMQ/Kafka)
- 📝 Centralized logging (ELK Stack)
- 📈 Monitoring and tracing (Prometheus, Grafana, Zipkin)
- 🐳 Docker containerization
- ☸️ Kubernetes orchestration
- 🧪 Unit and integration tests
- 📱 Mobile app (React Native)

---

## 👤 Author

**Pranshu Saraswat**  
BE Computer Science  
Clinic Management System – Microservices Project

---

## 📄 License

This project is for educational and demonstration purposes.

---

## 🤝 Contributing

Feel free to fork this repository and submit pull requests for improvements!

---

## 📞 Support

For issues and questions, please check:
1. [Setup Guide](SETUP_GUIDE.md) - Comprehensive troubleshooting
2. [Frontend README](frontend/README.md) - Frontend-specific guidance
3. GitHub Issues - Report bugs and request features

---

**Built with ❤️ using Spring Boot and Microservices Architecture**
