# Clinic Management System - Frontend

A modern, responsive web frontend for the Clinic Management System microservices architecture.

## 🚀 Features

### Doctor Management
- ✅ Create new doctors with name, specialization, and availability
- ✅ View all doctors in a sortable table
- ✅ Update doctor information
- ✅ Delete doctors
- ✅ Track doctor availability status

### Patient Management
- ✅ Register new patients with name, age, and gender
- ✅ View all patients
- ✅ Update patient information
- ✅ Delete patients

### Appointment Management
- ✅ Book appointments with patient and doctor selection
- ✅ Date picker for scheduling
- ✅ View all appointments
- ✅ Cancel appointments
- ✅ Automatic notification sending on booking
- ✅ Automatic bill creation on booking

### Billing Management
- ✅ Create bills for appointments
- ✅ Search bills by patient ID
- ✅ View bill amount and status (PAID/UNPAID)
- ✅ Mark bills as paid
- ✅ Delete bills

### Dashboard
- 📊 Real-time statistics overview
- 📈 Total doctors, patients, and appointments
- 👨‍⚕️ Available doctors count
- 🔄 Refresh capability

## 🛠️ Technologies Used

- **HTML5** - Semantic markup
- **CSS3** - Modern styling with Flexbox and Grid
- **Vanilla JavaScript** - No frameworks, pure JS
- **Fetch API** - RESTful API communication
- **Responsive Design** - Mobile-friendly layout

## 📋 Prerequisites

Before running the frontend, ensure all backend services are running:

1. **Eureka Server** - Port 8761
2. **API Gateway** - Port 8080
3. **Doctor Service** - Registers with Eureka
4. **Patient Service** - Registers with Eureka
5. **Appointment Service** - Registers with Eureka
6. **Billing Service** - Registers with Eureka
7. **Notification Service** - Registers with Eureka

## 🚀 Getting Started

### Option 1: Open Directly in Browser

1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```

2. Open `index.html` in your web browser:
   - Double-click the file, or
   - Right-click → Open with → Browser
   - Or use a local server (recommended)

### Option 2: Using VS Code Live Server (Recommended)

1. Install the **Live Server** extension in VS Code

2. Right-click on `index.html`

3. Select "Open with Live Server"

4. The application will open at `http://localhost:5500` (or similar)

### Option 3: Using Python HTTP Server

```bash
cd frontend
python -m http.server 8000
```

Then open: `http://localhost:8000`

### Option 4: Using Node.js HTTP Server

```bash
cd frontend
npx http-server -p 8000
```

Then open: `http://localhost:8000`

## 🔧 Configuration

The API Gateway URL is configured in `app.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080';
```

If your API Gateway runs on a different port, update this value.

## 📖 Usage Guide

### 1. Starting the Application

1. Ensure all backend microservices are running
2. Open the frontend in your browser
3. The dashboard will load automatically

### 2. Managing Doctors

- Click **"👨‍⚕️ Doctors"** in the navigation
- Click **"+ Add Doctor"** to create a new doctor
- Fill in name, specialization, and availability
- Click **"Edit"** on any doctor to update their information
- Click **"Delete"** to remove a doctor

### 3. Managing Patients

- Click **"🧑‍🦱 Patients"** in the navigation
- Click **"+ Add Patient"** to register a new patient
- Fill in name, age, and gender
- Use **"Edit"** and **"Delete"** buttons as needed

### 4. Booking Appointments

- Click **"📅 Appointments"** in the navigation
- Click **"+ Book Appointment"**
- Select a patient from the dropdown
- Select an available doctor
- Choose an appointment date
- Click **"Book Appointment"**
- The system will:
  - Create the appointment
  - Send an email notification
  - Generate a bill automatically

### 5. Managing Bills

- Click **"💰 Billing"** in the navigation
- Enter a **Patient ID** and click **"Search"** to view bills
- Click **"Mark as Paid"** to update payment status
- Click **"Delete"** to remove a bill
- Use **"+ Create Bill"** to manually create a bill

### 6. Viewing Dashboard

- Click **"📊 Dashboard"** in the navigation
- View real-time statistics
- Click **"🔄 Refresh"** to update the data

## 🎨 UI Features

### Toast Notifications
- Success messages (green)
- Error messages (red)
- Info messages (teal)
- Auto-dismiss after 3 seconds

### Loading Overlay
- Displays during API requests
- Prevents multiple submissions
- Shows loading spinner

### Responsive Design
- Desktop-optimized layout
- Tablet-friendly tables
- Mobile-responsive forms
- Adaptive navigation

### Status Badges
- Doctor availability (Available/Unavailable)
- Bill status (PAID/UNPAID)
- Color-coded for quick recognition

## 🔌 API Endpoints Used

### Doctor Service
- `GET /doctors` - List all doctors
- `POST /doctors` - Create doctor
- `GET /doctors/{id}` - Get doctor by ID
- `PUT /doctors/{id}` - Update doctor
- `DELETE /doctors/{id}` - Delete doctor

### Patient Service
- `GET /patients` - List all patients
- `POST /patients` - Create patient
- `GET /patients/{id}` - Get patient by ID
- `PUT /patients/{id}` - Update patient
- `DELETE /patients/{id}` - Delete patient

### Appointment Service
- `GET /appointments` - List all appointments
- `POST /appointments` - Book appointment
- `GET /appointments/{id}` - Get appointment by ID
- `DELETE /appointments/{id}` - Cancel appointment

### Billing Service
- `POST /bills?appointmentId={id}&patientId={id}` - Create bill
- `GET /bills/{id}` - Get bill by ID
- `GET /bills/patient/{patientId}` - Get bills by patient
- `PUT /bills/{id}/pay` - Mark bill as paid
- `DELETE /bills/{id}` - Delete bill

## 🐛 Troubleshooting

### CORS Errors
If you see CORS errors in the browser console, you may need to enable CORS in your API Gateway. Add this to your Spring Boot application:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}
```

### Connection Refused
- Ensure the API Gateway is running on port 8080
- Check that Eureka Server is running
- Verify all microservices are registered with Eureka

### No Data Displayed
- Check browser console for errors
- Verify backend services have data
- Test API endpoints directly using Postman

### Bills Not Loading
- Bills are loaded on-demand by patient ID
- Use the search feature to find bills
- Ensure bills exist for the searched patient

## 📱 Browser Compatibility

- ✅ Chrome (recommended)
- ✅ Firefox
- ✅ Edge
- ✅ Safari
- ⚠️ Internet Explorer (not supported)

## 🎯 Best Practices

1. Always start backend services before opening the frontend
2. Use the dashboard to get an overview before performing operations
3. Create doctors and patients before booking appointments
4. Search bills by patient ID for better performance
5. Check browser console for detailed error messages

## 📝 File Structure

```
frontend/
├── index.html      # Main HTML structure
├── styles.css      # All CSS styles
├── app.js          # JavaScript logic and API calls
└── README.md       # This file
```

## 🤝 Contributing

This frontend is designed to work with the Clinic Management System microservices backend. To extend functionality:

1. Add new sections in `index.html`
2. Style them in `styles.css`
3. Implement logic in `app.js`
4. Follow the existing patterns for consistency

## 📄 License

This project is part of the Clinic Management System microservices architecture demonstration.

## 👥 Support

For issues or questions:
1. Check the browser console for errors
2. Verify all backend services are running
3. Review the API Gateway logs
4. Check Eureka dashboard at http://localhost:8761

---

**Built with ❤️ for the Clinic Management System**
