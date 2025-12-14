# 🏥 Clinic Management System - Complete Setup Guide

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Starting the Backend](#starting-the-backend)
3. [Starting the Frontend](#starting-the-frontend)
4. [Testing the System](#testing-the-system)
5. [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

### Required Software
- **Java 21** (JDK)
- **Maven** (3.6+)
- **MySQL** (8.0+) or use H2 (in-memory database)
- **Modern Web Browser** (Chrome, Firefox, Edge)

### Port Requirements
Ensure these ports are available:
- **8761** - Eureka Server
- **8080** - API Gateway
- **8081** - Doctor Service
- **8082** - Patient Service
- **8083** - Appointment Service
- **8084** - Billing Service
- **8085** - Notification Service

---

## 🚀 Starting the Backend

### Option 1: Start All Services (PowerShell)

Create a script named `start-all-services.ps1` in the project root:

```powershell
# Start Eureka Server
Write-Host "Starting Eureka Server..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd eureka-server; mvn spring-boot:run"
Start-Sleep -Seconds 10

# Start Doctor Service
Write-Host "Starting Doctor Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd doctor-service; mvn spring-boot:run"
Start-Sleep -Seconds 5

# Start Patient Service
Write-Host "Starting Patient Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd patient-service; mvn spring-boot:run"
Start-Sleep -Seconds 5

# Start Appointment Service
Write-Host "Starting Appointment Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd appointment-service; mvn spring-boot:run"
Start-Sleep -Seconds 5

# Start Billing Service
Write-Host "Starting Billing Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd billing-service; mvn spring-boot:run"
Start-Sleep -Seconds 5

# Start Notification Service
Write-Host "Starting Notification Service..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd notification-service; mvn spring-boot:run"
Start-Sleep -Seconds 5

# Start API Gateway
Write-Host "Starting API Gateway..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd api-gateway; mvn spring-boot:run"

Write-Host "`nAll services are starting! Check individual terminals for status." -ForegroundColor Cyan
Write-Host "Eureka Dashboard: http://localhost:8761" -ForegroundColor Yellow
Write-Host "API Gateway: http://localhost:8080" -ForegroundColor Yellow
```

Run the script:
```powershell
.\start-all-services.ps1
```

### Option 2: Start Services Manually (Recommended for First Time)

Open **7 separate terminals** and run these commands in order:

#### Terminal 1: Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Wait until you see: "Started EurekaServerApplication"

#### Terminal 2: Doctor Service
```bash
cd doctor-service
mvn spring-boot:run
```
Wait until you see: "Started DoctorServiceApplication"

#### Terminal 3: Patient Service
```bash
cd patient-service
mvn spring-boot:run
```
Wait until you see: "Started PatientServiceApplication"

#### Terminal 4: Appointment Service
```bash
cd appointment-service
mvn spring-boot:run
```
Wait until you see: "Started AppointmentServiceApplication"

#### Terminal 5: Billing Service
```bash
cd billing-service
mvn spring-boot:run
```
Wait until you see: "Started BillingServiceApplication"

#### Terminal 6: Notification Service
```bash
cd notification-service
mvn spring-boot:run
```
Wait until you see: "Started NotificationServiceApplication"

#### Terminal 7: API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
Wait until you see: "Started ApiGatewayApplication"

### ✅ Verify Services are Running

1. Open Eureka Dashboard: http://localhost:8761
2. You should see all services registered:
   - DOCTOR-SERVICE
   - PATIENT-SERVICE
   - APPOINTMENT-SERVICE
   - BILLING-SERVICE
   - NOTIFICATION-SERVICE
   - API-GATEWAY

---

## 🎨 Starting the Frontend

### Option 1: Using Browser Directly
```bash
cd frontend
# Open index.html in your browser (double-click or drag to browser)
```

### Option 2: Using VS Code Live Server
1. Open VS Code
2. Install "Live Server" extension
3. Right-click on `index.html`
4. Select "Open with Live Server"

### Option 3: Using Python
```bash
cd frontend
python -m http.server 8000
```
Then open: http://localhost:8000

### Option 4: Using Node.js
```bash
cd frontend
npx http-server -p 8000
```
Then open: http://localhost:8000

---

## 🧪 Testing the System

### 1. Test Backend Services (Using PowerShell)

```powershell
# Test Doctor Service
Invoke-WebRequest -Uri "http://localhost:8080/doctors" -Method Get

# Test Patient Service
Invoke-WebRequest -Uri "http://localhost:8080/patients" -Method Get

# Test Appointment Service
Invoke-WebRequest -Uri "http://localhost:8080/appointments" -Method Get
```

### 2. Test Frontend

1. Open the frontend in your browser
2. Navigate to **Dashboard** - should show 0s initially
3. Add a Doctor:
   - Click "Doctors" tab
   - Click "+ Add Doctor"
   - Fill form and submit
4. Add a Patient:
   - Click "Patients" tab
   - Click "+ Add Patient"
   - Fill form and submit
5. Book an Appointment:
   - Click "Appointments" tab
   - Click "+ Book Appointment"
   - Select patient and doctor
   - Choose date and submit
   - ✅ System should:
     - Create appointment
     - Send email notification
     - Create bill automatically
6. View Bills:
   - Click "Billing" tab
   - Enter patient ID
   - Click "Search"
   - Should see the auto-created bill

### 3. End-to-End Flow

```
1. Create Doctor (Dr. Smith, Cardiology)
   ↓
2. Create Patient (John Doe, Age 30, Male)
   ↓
3. Book Appointment (John Doe → Dr. Smith → Tomorrow)
   ↓
4. System Automatically:
   - Validates patient exists ✅
   - Validates doctor exists ✅
   - Creates appointment ✅
   - Triggers notification email 📧
   - Creates bill (UNPAID) 💰
   ↓
5. Search Bills by Patient ID
   ↓
6. Mark Bill as PAID ✅
```

---

## 🐛 Troubleshooting

### Issue: CORS Errors in Browser

**Symptom:** Console shows "CORS policy" errors

**Solution:**
1. Ensure you added `CorsConfig.java` to api-gateway
2. Rebuild and restart API Gateway:
   ```bash
   cd api-gateway
   mvn clean install
   mvn spring-boot:run
   ```

### Issue: Services Not Registering with Eureka

**Symptom:** Services don't appear in Eureka dashboard

**Solution:**
1. Verify Eureka is running first
2. Check each service's `application.properties` or `application.yml`
3. Ensure they have:
   ```yaml
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka
   ```

### Issue: Connection Refused Errors

**Symptom:** Frontend shows "Connection refused"

**Solution:**
1. Verify API Gateway is running on port 8080
2. Check if port is already in use:
   ```powershell
   netstat -ano | findstr :8080
   ```
3. Kill process if needed:
   ```powershell
   taskkill /PID <process_id> /F
   ```

### Issue: Database Errors

**Symptom:** Services fail to start with DB errors

**Solution:**
1. Check if using H2 (in-memory) or MySQL
2. For H2, ensure this is in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.jpa.hibernate.ddl-auto=update
   ```
3. For MySQL, ensure database exists and credentials are correct

### Issue: Port Already in Use

**Symptom:** "Port 8080 is already in use"

**Solution:**
```powershell
# Find process using port
netstat -ano | findstr :<PORT>

# Kill the process
taskkill /PID <process_id> /F
```

### Issue: Email Notifications Not Sending

**Symptom:** Appointment created but no email

**Solution:**
1. Check Notification Service logs
2. Verify SMTP credentials in notification-service `application.properties`
3. For Gmail, ensure:
   - Less secure apps enabled OR
   - App-specific password created

### Issue: Frontend Shows No Data

**Symptom:** Tables show "Loading..." forever

**Solution:**
1. Open browser console (F12)
2. Check for error messages
3. Verify API Gateway URL in `app.js`:
   ```javascript
   const API_BASE_URL = 'http://localhost:8080';
   ```
4. Test API directly:
   ```powershell
   Invoke-WebRequest -Uri "http://localhost:8080/doctors" -Method Get
   ```

---

## 📊 Monitoring & Management

### Eureka Dashboard
```
http://localhost:8761
```
- View all registered services
- Check service health
- See instance details

### API Gateway Routes
```
http://localhost:8080/actuator/gateway/routes
```
(If actuator is enabled)

### Service Health Checks
```powershell
# Check if service is responding
Invoke-WebRequest -Uri "http://localhost:8080/doctors" -Method Get
Invoke-WebRequest -Uri "http://localhost:8080/patients" -Method Get
Invoke-WebRequest -Uri "http://localhost:8080/appointments" -Method Get
Invoke-WebRequest -Uri "http://localhost:8080/bills/patient/1" -Method Get
```

---

## 🛑 Stopping Services

### Graceful Shutdown Order (Reverse of startup)

1. Stop API Gateway (Ctrl+C in terminal 7)
2. Stop Notification Service (Ctrl+C in terminal 6)
3. Stop Billing Service (Ctrl+C in terminal 5)
4. Stop Appointment Service (Ctrl+C in terminal 4)
5. Stop Patient Service (Ctrl+C in terminal 3)
6. Stop Doctor Service (Ctrl+C in terminal 2)
7. Stop Eureka Server (Ctrl+C in terminal 1)

### Force Kill All (if terminals are closed)

```powershell
# Kill all Java processes (careful!)
Get-Process java | Stop-Process -Force
```

---

## 📚 Additional Resources

### Project Structure
```
ClinicSystem/
├── eureka-server/          # Service Discovery
├── api-gateway/            # Entry Point
├── doctor-service/         # Doctor Management
├── patient-service/        # Patient Management
├── appointment-service/    # Orchestration Service
├── billing-service/        # Financial Records
├── notification-service/   # Email Notifications
└── frontend/              # Web UI
    ├── index.html
    ├── styles.css
    ├── app.js
    └── README.md
```

### API Endpoints Summary

| Service | Endpoint | Description |
|---------|----------|-------------|
| Doctor | GET /doctors | List all doctors |
| Doctor | POST /doctors | Create doctor |
| Doctor | PUT /doctors/{id} | Update doctor |
| Doctor | DELETE /doctors/{id} | Delete doctor |
| Patient | GET /patients | List all patients |
| Patient | POST /patients | Create patient |
| Patient | PUT /patients/{id} | Update patient |
| Patient | DELETE /patients/{id} | Delete patient |
| Appointment | GET /appointments | List all appointments |
| Appointment | POST /appointments | Book appointment |
| Appointment | DELETE /appointments/{id} | Cancel appointment |
| Billing | POST /bills | Create bill |
| Billing | GET /bills/patient/{id} | Get patient bills |
| Billing | PUT /bills/{id}/pay | Mark as paid |
| Billing | DELETE /bills/{id} | Delete bill |

---

## 🎯 Quick Start Checklist

- [ ] Java 21 installed
- [ ] Maven installed
- [ ] All ports (8761, 8080-8085) available
- [ ] Started Eureka Server
- [ ] Started all microservices
- [ ] Verified services in Eureka dashboard
- [ ] Added CORS configuration to API Gateway
- [ ] Opened frontend in browser
- [ ] Created test doctor
- [ ] Created test patient
- [ ] Booked test appointment
- [ ] Verified bill creation
- [ ] System working end-to-end ✅

---

## 💡 Tips

1. **Always start Eureka first** - Other services need it for registration
2. **Wait between starts** - Give each service 5-10 seconds to register
3. **Check Eureka dashboard** - Verify all services are UP before testing
4. **Use browser console** - Great for debugging frontend issues
5. **Check terminal logs** - Backend errors appear in service terminals
6. **Test APIs with Postman** - Validate backend before testing frontend

---

## 🤝 Support

If you encounter issues:
1. Check this troubleshooting guide
2. Review terminal logs for errors
3. Verify all services are UP in Eureka
4. Check browser console for frontend errors
5. Test APIs directly with Postman or PowerShell

---

**Happy Clinic Managing! 🏥**
