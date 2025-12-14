# 🚀 Quick Reference Guide

## One-Page Cheat Sheet for Clinic Management System Frontend

---

## 📁 Project Structure
```
ClinicSystem/
├── frontend/
│   ├── index.html       ← Main HTML file
│   ├── styles.css       ← All styling
│   ├── app.js           ← JavaScript logic
│   └── README.md        ← Frontend docs
├── start-all-services.ps1   ← PowerShell startup script
├── start-all-services.bat   ← Batch startup script
├── SETUP_GUIDE.md           ← Complete setup guide
├── ARCHITECTURE.md          ← System diagrams
└── README_NEW.md            ← Updated main README
```

---

## 🚀 Quick Start Commands

### Start All Services
```powershell
.\start-all-services.ps1
```

### Start Frontend
```bash
cd frontend
# Then open index.html in browser
```

### Check Services
```
http://localhost:8761  ← Eureka Dashboard
http://localhost:8080  ← API Gateway
```

---

## 🔌 API Endpoints Reference

### Doctors
```
GET    /doctors          List all
POST   /doctors          Create new
GET    /doctors/{id}     Get by ID
PUT    /doctors/{id}     Update
DELETE /doctors/{id}     Delete
```

### Patients
```
GET    /patients         List all
POST   /patients         Create new
GET    /patients/{id}    Get by ID
PUT    /patients/{id}    Update
DELETE /patients/{id}    Delete
```

### Appointments
```
GET    /appointments         List all
POST   /appointments         Book new (triggers billing + email)
GET    /appointments/{id}    Get by ID
DELETE /appointments/{id}    Cancel
```

### Billing
```
POST   /bills?appointmentId={}&patientId={}   Create bill
GET    /bills/{id}                             Get by ID
GET    /bills/patient/{patientId}              Get all for patient
PUT    /bills/{id}/pay                         Mark as paid
DELETE /bills/{id}                             Delete
```

---

## 🎯 API Base URL

All requests go through API Gateway:
```javascript
const API_BASE_URL = 'http://localhost:8080';
```

Change this in `app.js` if your gateway is on a different port.

---

## 🎨 Key Files to Modify

### Change Colors
📁 **File**: `frontend/styles.css`  
📍 **Lines**: 10-19 (CSS variables)
```css
:root {
    --primary-color: #2c3e50;
    --secondary-color: #3498db;
    /* Change these colors */
}
```

### Change API URL
📁 **File**: `frontend/app.js`  
📍 **Line**: 4
```javascript
const API_BASE_URL = 'http://localhost:8080';
```

### Add New Section
📁 **File**: `frontend/index.html`  
📍 **Section**: Add after line 350
```html
<section id="new-section" class="content-section">
    <!-- Your content -->
</section>
```

---

## 🐛 Common Issues & Fixes

### Issue: CORS Error
**Fix**: Add `CorsConfig.java` to API Gateway
```
File created at:
api-gateway/src/main/java/com/clinic/apigateway/config/CorsConfig.java
```
Restart API Gateway after adding.

### Issue: Connection Refused
**Fix**: Ensure API Gateway is running
```powershell
# Check if running
netstat -ano | findstr :8080
```

### Issue: Services Not Showing in Eureka
**Fix**: Wait 30 seconds, then refresh Eureka dashboard

### Issue: Bills Not Loading
**Fix**: Bills load by patient ID search, not automatically

### Issue: Frontend Shows No Data
**Fix**: 
1. Check browser console (F12)
2. Verify services are UP in Eureka
3. Test API directly: `http://localhost:8080/doctors`

---

## 📋 Testing Checklist

### Backend Health Check
- [ ] Eureka running: http://localhost:8761
- [ ] 6 services registered and UP
- [ ] API Gateway responding: http://localhost:8080/doctors

### Frontend Test Flow
- [ ] Dashboard loads and shows statistics
- [ ] Can add a doctor
- [ ] Can add a patient
- [ ] Can book appointment
- [ ] Bill created automatically
- [ ] Can search bills by patient
- [ ] Can mark bill as paid
- [ ] Toast notifications appear
- [ ] Loading overlay shows during API calls

---

## 🎯 Port Reference

| Service               | Port | URL                          |
|-----------------------|------|------------------------------|
| Eureka Server         | 8761 | http://localhost:8761        |
| API Gateway           | 8080 | http://localhost:8080        |
| Doctor Service        | 8081 | (internal)                   |
| Patient Service       | 8082 | (internal)                   |
| Appointment Service   | 8083 | (internal)                   |
| Billing Service       | 8084 | (internal)                   |
| Notification Service  | 8085 | (internal)                   |

---

## 🔧 Configuration Files

### API Gateway Routes
📁 `api-gateway/src/main/resources/application.yml`
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: doctor-service
          uri: lb://DOCTOR-SERVICE
          predicates:
            - Path=/doctors/**
```

### Service Registration
📁 Each service's `application.properties` or `application.yml`
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

---

## 📚 Documentation Files

| File                          | Purpose                              |
|-------------------------------|--------------------------------------|
| `frontend/README.md`          | Frontend-specific documentation      |
| `SETUP_GUIDE.md`              | Complete setup with troubleshooting  |
| `ARCHITECTURE.md`             | System architecture diagrams         |
| `FRONTEND_IMPLEMENTATION.md`  | Implementation details               |
| `UI_PREVIEW.md`               | Visual UI component guide            |
| `README_NEW.md`               | Updated project README               |

---

## 🎨 CSS Class Reference

### Buttons
```css
.btn-primary    → Blue (default action)
.btn-success    → Green (save, confirm)
.btn-danger     → Red (delete, cancel)
.btn-warning    → Orange (warnings)
.btn-info       → Teal (information)
.btn-secondary  → Gray (cancel, back)
.btn-sm         → Small size
```

### Status Badges
```css
.status-available   → Green badge
.status-unavailable → Red badge
.status-paid        → Green badge
.status-unpaid      → Yellow badge
```

### Layout
```css
.container      → Max-width container
.content-section → Main content area
.form-card      → Form container
.data-table     → Table wrapper
.dashboard-grid → Grid layout for cards
```

---

## 🔑 Key Functions in app.js

### Navigation
```javascript
showSection(sectionName)  // Switch between tabs
```

### Doctors
```javascript
loadDoctors()             // Fetch and display all doctors
addDoctor(event)          // Create new doctor
updateDoctor(event)       // Update existing doctor
deleteDoctor(id)          // Delete doctor by ID
```

### Patients
```javascript
loadPatients()            // Fetch and display all patients
addPatient(event)         // Create new patient
updatePatient(event)      // Update existing patient
deletePatient(id)         // Delete patient by ID
```

### Appointments
```javascript
loadAppointments()        // Fetch and display all appointments
addAppointment(event)     // Book new appointment
deleteAppointment(id)     // Cancel appointment
```

### Billing
```javascript
searchBillsByPatient()    // Search bills by patient ID
addBill(event)            // Create new bill
payBill(id)               // Mark bill as paid
deleteBill(id)            // Delete bill
```

### Dashboard
```javascript
refreshDashboard()        // Update all statistics
```

### Utilities
```javascript
showToast(msg, type)      // Show notification
showLoading()             // Show loading overlay
hideLoading()             // Hide loading overlay
apiRequest(url, options)  // Make API call
```

---

## 🎯 Workflow: Book Appointment

```
1. Click "Appointments" tab
2. Click "+ Book Appointment"
3. Select patient from dropdown
4. Select doctor from dropdown
5. Choose appointment date
6. Click "Book Appointment"
7. System automatically:
   ✓ Validates patient exists
   ✓ Validates doctor exists
   ✓ Creates appointment
   ✓ Creates bill (UNPAID)
   ✓ Sends email notification
8. Success toast appears
9. Appointment appears in table
```

---

## 📊 Data Models

### Doctor
```javascript
{
  id: 1,
  name: "Dr. Smith",
  specialization: "Cardiology",
  available: true
}
```

### Patient
```javascript
{
  id: 1,
  name: "John Doe",
  age: 30,
  gender: "Male"
}
```

### Appointment
```javascript
{
  id: 1,
  patientId: 1,
  doctorId: 1,
  appointmentDate: "2025-12-15"
}
```

### Bill
```javascript
{
  id: 1,
  appointmentId: 1,
  patientId: 1,
  amount: 100.00,
  status: "UNPAID"  // or "PAID"
}
```

---

## 🚨 Emergency Commands

### Kill All Java Processes
```powershell
Get-Process java | Stop-Process -Force
```

### Find What's Using a Port
```powershell
netstat -ano | findstr :8080
```

### Kill Specific Process
```powershell
taskkill /PID <process_id> /F
```

---

## 💡 Pro Tips

1. **Always check Eureka first** when things don't work
2. **Use browser console** (F12) for debugging
3. **Wait 30 seconds** after starting services
4. **Bills load on-demand** - use the search feature
5. **Test backend first** with Postman before blaming frontend
6. **CORS must be enabled** in API Gateway
7. **Services must be UP** in Eureka before testing

---

## 📞 Need Help?

1. Check **SETUP_GUIDE.md** for detailed troubleshooting
2. Review **ARCHITECTURE.md** for system understanding
3. Read **frontend/README.md** for frontend-specific issues
4. Check browser console for error messages
5. Verify all services are UP in Eureka dashboard

---

## ✅ Success Indicators

✅ Eureka shows 6 services UP  
✅ API Gateway responds to requests  
✅ Frontend loads without console errors  
✅ Dashboard shows correct statistics  
✅ Forms submit successfully  
✅ Toast notifications appear  
✅ Data displays in tables  
✅ Appointment booking triggers billing  

---

## 🎉 You're Ready!

**All the information you need is in these files:**

- `QUICK_REFERENCE.md` ← You are here
- `SETUP_GUIDE.md` ← Detailed setup
- `frontend/README.md` ← Frontend guide
- `ARCHITECTURE.md` ← System diagrams

**Happy Clinic Managing! 🏥**
