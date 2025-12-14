// ===========================
// Configuration
// ===========================
const API_BASE_URL = 'http://localhost:8080';

// ===========================
// Utility Functions
// ===========================

function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function showLoading() {
    document.getElementById('loading-overlay').classList.remove('hidden');
}

function hideLoading() {
    document.getElementById('loading-overlay').classList.add('hidden');
}

async function apiRequest(url, options = {}) {
    try {
        showLoading();
        const response = await fetch(url, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }
        
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        }
        return null;
    } catch (error) {
        console.error('API Error:', error);
        showToast(error.message || 'An error occurred', 'error');
        throw error;
    } finally {
        hideLoading();
    }
}

// ===========================
// Navigation
// ===========================

function showSection(sectionName) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remove active class from all nav buttons
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(`${sectionName}-section`).classList.add('active');
    
    // Add active class to clicked button
    event.target.classList.add('active');
    
    // Load data for the section
    switch(sectionName) {
        case 'doctors':
            loadDoctors();
            break;
        case 'patients':
            loadPatients();
            break;
        case 'appointments':
            loadAppointments();
            break;
        case 'billing':
            // Bills are loaded on demand, not automatically
            break;
        case 'dashboard':
            refreshDashboard();
            break;
    }
}

// ===========================
// Doctors Management
// ===========================

async function loadDoctors() {
    try {
        const doctors = await apiRequest(`${API_BASE_URL}/doctors`);
        const tbody = document.getElementById('doctors-tbody');
        
        if (doctors.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="loading">No doctors found. Add a doctor to get started.</td></tr>';
            return;
        }
        
        tbody.innerHTML = doctors.map(doctor => `
            <tr>
                <td>${doctor.id}</td>
                <td>${doctor.name}</td>
                <td>${doctor.specialization}</td>
                <td>
                    <span class="status-badge ${doctor.available ? 'status-available' : 'status-unavailable'}">
                        ${doctor.available ? 'Available' : 'Unavailable'}
                    </span>
                </td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="showEditDoctorForm(${doctor.id})">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteDoctor(${doctor.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        document.getElementById('doctors-tbody').innerHTML = 
            '<tr><td colspan="5" class="loading">Error loading doctors</td></tr>';
    }
}

function showAddDoctorForm() {
    document.getElementById('add-doctor-form').classList.remove('hidden');
    document.getElementById('update-doctor-form').classList.add('hidden');
}

function hideAddDoctorForm() {
    document.getElementById('add-doctor-form').classList.add('hidden');
    document.getElementById('doctor-name').value = '';
    document.getElementById('doctor-specialization').value = '';
    document.getElementById('doctor-available').checked = true;
}

async function addDoctor(event) {
    event.preventDefault();
    
    const doctor = {
        name: document.getElementById('doctor-name').value,
        specialization: document.getElementById('doctor-specialization').value,
        available: document.getElementById('doctor-available').checked
    };
    
    try {
        await apiRequest(`${API_BASE_URL}/doctors`, {
            method: 'POST',
            body: JSON.stringify(doctor)
        });
        
        showToast('Doctor added successfully!', 'success');
        hideAddDoctorForm();
        loadDoctors();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function showEditDoctorForm(id) {
    try {
        const doctor = await apiRequest(`${API_BASE_URL}/doctors/${id}`);
        
        document.getElementById('update-doctor-id').value = doctor.id;
        document.getElementById('update-doctor-name').value = doctor.name;
        document.getElementById('update-doctor-specialization').value = doctor.specialization;
        document.getElementById('update-doctor-available').checked = doctor.available;
        
        document.getElementById('update-doctor-form').classList.remove('hidden');
        document.getElementById('add-doctor-form').classList.add('hidden');
        
        // Scroll to form
        document.getElementById('update-doctor-form').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        // Error already handled by apiRequest
    }
}

function hideUpdateDoctorForm() {
    document.getElementById('update-doctor-form').classList.add('hidden');
}

async function updateDoctor(event) {
    event.preventDefault();
    
    const id = document.getElementById('update-doctor-id').value;
    const doctor = {
        name: document.getElementById('update-doctor-name').value,
        specialization: document.getElementById('update-doctor-specialization').value,
        available: document.getElementById('update-doctor-available').checked
    };
    
    try {
        await apiRequest(`${API_BASE_URL}/doctors/${id}`, {
            method: 'PUT',
            body: JSON.stringify(doctor)
        });
        
        showToast('Doctor updated successfully!', 'success');
        hideUpdateDoctorForm();
        loadDoctors();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function deleteDoctor(id) {
    if (!confirm('Are you sure you want to delete this doctor?')) {
        return;
    }
    
    try {
        await apiRequest(`${API_BASE_URL}/doctors/${id}`, {
            method: 'DELETE'
        });
        
        showToast('Doctor deleted successfully!', 'success');
        loadDoctors();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

// ===========================
// Patients Management
// ===========================

async function loadPatients() {
    try {
        const patients = await apiRequest(`${API_BASE_URL}/patients`);
        const tbody = document.getElementById('patients-tbody');
        
        if (patients.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="loading">No patients found. Add a patient to get started.</td></tr>';
            return;
        }
        
        tbody.innerHTML = patients.map(patient => `
            <tr>
                <td>${patient.id}</td>
                <td>${patient.name}</td>
                <td>${patient.age}</td>
                <td>${patient.gender}</td>
                <td>
                    <button class="btn btn-info btn-sm" onclick="showEditPatientForm(${patient.id})">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deletePatient(${patient.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        document.getElementById('patients-tbody').innerHTML = 
            '<tr><td colspan="5" class="loading">Error loading patients</td></tr>';
    }
}

function showAddPatientForm() {
    document.getElementById('add-patient-form').classList.remove('hidden');
    document.getElementById('update-patient-form').classList.add('hidden');
}

function hideAddPatientForm() {
    document.getElementById('add-patient-form').classList.add('hidden');
    document.getElementById('patient-name').value = '';
    document.getElementById('patient-age').value = '';
    document.getElementById('patient-gender').value = '';
}

async function addPatient(event) {
    event.preventDefault();
    
    const patient = {
        name: document.getElementById('patient-name').value,
        age: parseInt(document.getElementById('patient-age').value),
        gender: document.getElementById('patient-gender').value
    };
    
    try {
        await apiRequest(`${API_BASE_URL}/patients`, {
            method: 'POST',
            body: JSON.stringify(patient)
        });
        
        showToast('Patient added successfully!', 'success');
        hideAddPatientForm();
        loadPatients();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function showEditPatientForm(id) {
    try {
        const patient = await apiRequest(`${API_BASE_URL}/patients/${id}`);
        
        document.getElementById('update-patient-id').value = patient.id;
        document.getElementById('update-patient-name').value = patient.name;
        document.getElementById('update-patient-age').value = patient.age;
        document.getElementById('update-patient-gender').value = patient.gender;
        
        document.getElementById('update-patient-form').classList.remove('hidden');
        document.getElementById('add-patient-form').classList.add('hidden');
        
        // Scroll to form
        document.getElementById('update-patient-form').scrollIntoView({ behavior: 'smooth' });
    } catch (error) {
        // Error already handled by apiRequest
    }
}

function hideUpdatePatientForm() {
    document.getElementById('update-patient-form').classList.add('hidden');
}

async function updatePatient(event) {
    event.preventDefault();
    
    const id = document.getElementById('update-patient-id').value;
    const patient = {
        name: document.getElementById('update-patient-name').value,
        age: parseInt(document.getElementById('update-patient-age').value),
        gender: document.getElementById('update-patient-gender').value
    };
    
    try {
        await apiRequest(`${API_BASE_URL}/patients/${id}`, {
            method: 'PUT',
            body: JSON.stringify(patient)
        });
        
        showToast('Patient updated successfully!', 'success');
        hideUpdatePatientForm();
        loadPatients();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function deletePatient(id) {
    if (!confirm('Are you sure you want to delete this patient?')) {
        return;
    }
    
    try {
        await apiRequest(`${API_BASE_URL}/patients/${id}`, {
            method: 'DELETE'
        });
        
        showToast('Patient deleted successfully!', 'success');
        loadPatients();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

// ===========================
// Appointments Management
// ===========================

async function loadAppointments() {
    try {
        const appointments = await apiRequest(`${API_BASE_URL}/appointments`);
        const tbody = document.getElementById('appointments-tbody');
        
        if (appointments.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="loading">No appointments found. Book an appointment to get started.</td></tr>';
            return;
        }
        
        tbody.innerHTML = appointments.map(appointment => `
            <tr>
                <td>${appointment.id}</td>
                <td>${appointment.patientId}</td>
                <td>${appointment.doctorId}</td>
                <td>${appointment.appointmentDate}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteAppointment(${appointment.id})">Cancel</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        document.getElementById('appointments-tbody').innerHTML = 
            '<tr><td colspan="5" class="loading">Error loading appointments</td></tr>';
    }
}

async function showAddAppointmentForm() {
    document.getElementById('add-appointment-form').classList.remove('hidden');
    
    // Load patients and doctors for dropdowns
    try {
        const [patients, doctors] = await Promise.all([
            apiRequest(`${API_BASE_URL}/patients`),
            apiRequest(`${API_BASE_URL}/doctors`)
        ]);
        
        const patientSelect = document.getElementById('appointment-patient');
        const doctorSelect = document.getElementById('appointment-doctor');
        
        patientSelect.innerHTML = '<option value="">Select Patient</option>' + 
            patients.map(p => `<option value="${p.id}">${p.name} (ID: ${p.id})</option>`).join('');
        
        doctorSelect.innerHTML = '<option value="">Select Doctor</option>' + 
            doctors.filter(d => d.available)
                   .map(d => `<option value="${d.id}">${d.name} - ${d.specialization} (ID: ${d.id})</option>`).join('');
    } catch (error) {
        // Error already handled by apiRequest
    }
}

function hideAddAppointmentForm() {
    document.getElementById('add-appointment-form').classList.add('hidden');
    document.getElementById('appointment-patient').value = '';
    document.getElementById('appointment-doctor').value = '';
    document.getElementById('appointment-date').value = '';
}

async function addAppointment(event) {
    event.preventDefault();
    
    const appointment = {
        patientId: parseInt(document.getElementById('appointment-patient').value),
        doctorId: parseInt(document.getElementById('appointment-doctor').value),
        appointmentDate: document.getElementById('appointment-date').value
    };
    
    try {
        await apiRequest(`${API_BASE_URL}/appointments`, {
            method: 'POST',
            body: JSON.stringify(appointment)
        });
        
        showToast('Appointment booked successfully! Notification sent and bill created.', 'success');
        hideAddAppointmentForm();
        loadAppointments();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function deleteAppointment(id) {
    if (!confirm('Are you sure you want to cancel this appointment?')) {
        return;
    }
    
    try {
        await apiRequest(`${API_BASE_URL}/appointments/${id}`, {
            method: 'DELETE'
        });
        
        showToast('Appointment cancelled successfully!', 'success');
        loadAppointments();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

// ===========================
// Billing Management
// ===========================

function loadAllBills() {
    document.getElementById('bills-tbody').innerHTML = 
        '<tr><td colspan="6" class="loading">No bills to display. Create a bill or search by patient ID.</td></tr>';
    showToast('Bills are loaded by patient ID. Use the search feature.', 'info');
}

async function searchBillsByPatient() {
    const patientId = document.getElementById('search-patient-id').value;
    
    if (!patientId) {
        showToast('Please enter a patient ID', 'error');
        return;
    }
    
    try {
        const bills = await apiRequest(`${API_BASE_URL}/bills/patient/${patientId}`);
        const tbody = document.getElementById('bills-tbody');
        
        if (bills.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="loading">No bills found for patient ID: ${patientId}</td></tr>`;
            return;
        }
        
        tbody.innerHTML = bills.map(bill => `
            <tr>
                <td>${bill.id}</td>
                <td>${bill.appointmentId}</td>
                <td>${bill.patientId}</td>
                <td>$${bill.amount.toFixed(2)}</td>
                <td>
                    <span class="status-badge ${bill.status === 'PAID' ? 'status-paid' : 'status-unpaid'}">
                        ${bill.status}
                    </span>
                </td>
                <td>
                    ${bill.status === 'UNPAID' ? 
                        `<button class="btn btn-success btn-sm" onclick="payBill(${bill.id})">Mark as Paid</button>` : 
                        '<span style="color: #27ae60;">✓ Paid</span>'}
                    <button class="btn btn-danger btn-sm" onclick="deleteBill(${bill.id})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        document.getElementById('bills-tbody').innerHTML = 
            '<tr><td colspan="6" class="loading">Error loading bills</td></tr>';
    }
}

function showAddBillForm() {
    document.getElementById('add-bill-form').classList.remove('hidden');
}

function hideAddBillForm() {
    document.getElementById('add-bill-form').classList.add('hidden');
    document.getElementById('bill-appointment-id').value = '';
    document.getElementById('bill-patient-id').value = '';
}

async function addBill(event) {
    event.preventDefault();
    
    const appointmentId = document.getElementById('bill-appointment-id').value;
    const patientId = document.getElementById('bill-patient-id').value;
    
    try {
        await apiRequest(`${API_BASE_URL}/bills?appointmentId=${appointmentId}&patientId=${patientId}`, {
            method: 'POST'
        });
        
        showToast('Bill created successfully!', 'success');
        hideAddBillForm();
        // Automatically search for the patient's bills
        document.getElementById('search-patient-id').value = patientId;
        searchBillsByPatient();
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function payBill(id) {
    try {
        await apiRequest(`${API_BASE_URL}/bills/${id}/pay`, {
            method: 'PUT'
        });
        
        showToast('Bill marked as paid!', 'success');
        // Re-search current patient
        const patientId = document.getElementById('search-patient-id').value;
        if (patientId) {
            searchBillsByPatient();
        }
    } catch (error) {
        // Error already handled by apiRequest
    }
}

async function deleteBill(id) {
    if (!confirm('Are you sure you want to delete this bill?')) {
        return;
    }
    
    try {
        await apiRequest(`${API_BASE_URL}/bills/${id}`, {
            method: 'DELETE'
        });
        
        showToast('Bill deleted successfully!', 'success');
        // Re-search current patient
        const patientId = document.getElementById('search-patient-id').value;
        if (patientId) {
            searchBillsByPatient();
        }
    } catch (error) {
        // Error already handled by apiRequest
    }
}

// ===========================
// Dashboard
// ===========================

async function refreshDashboard() {
    try {
        const [doctors, patients, appointments] = await Promise.all([
            apiRequest(`${API_BASE_URL}/doctors`),
            apiRequest(`${API_BASE_URL}/patients`),
            apiRequest(`${API_BASE_URL}/appointments`)
        ]);
        
        document.getElementById('total-doctors').textContent = doctors.length;
        document.getElementById('total-patients').textContent = patients.length;
        document.getElementById('total-appointments').textContent = appointments.length;
        document.getElementById('available-doctors').textContent = 
            doctors.filter(d => d.available).length;
        
        showToast('Dashboard refreshed!', 'info');
    } catch (error) {
        showToast('Error loading dashboard data', 'error');
    }
}

// ===========================
// Initialize on Page Load
// ===========================

window.addEventListener('DOMContentLoaded', () => {
    // Load initial data
    loadDoctors();
    refreshDashboard();
    
    // Set minimum date for appointments to today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('appointment-date').setAttribute('min', today);
    
    console.log('Clinic Management System initialized');
    console.log('API Gateway:', API_BASE_URL);
});
