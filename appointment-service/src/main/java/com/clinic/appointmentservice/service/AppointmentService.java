package com.clinic.appointmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.repository.AppointmentRepository;
import com.clinic.appointmentservice.model.Notification;

import com.clinic.appointmentservice.model.PatientDTO;
import com.clinic.appointmentservice.model.DoctorDTO;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final RestTemplate restTemplate;

    public AppointmentService(AppointmentRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public Appointment bookAppointment(Appointment appointment) {


    PatientDTO patient = restTemplate.getForObject(
        "http://PATIENT-SERVICE/patients/" + appointment.getPatientId(),
        PatientDTO.class
    );

  
    DoctorDTO doctor = restTemplate.getForObject(
        "http://DOCTOR-SERVICE/doctors/" + appointment.getDoctorId(),
        DoctorDTO.class
    );


    Appointment saved = repo.save(appointment);


    restTemplate.postForObject(
        "http://BILLING-SERVICE/bills?appointmentId="
            + saved.getId()
            + "&patientId="
            + saved.getPatientId(),
        null,
        Object.class
    );


    Notification notification = new Notification(
        "Appointment booked successfully.\n\n"
        + "Patient: " + patient.getName() + "\n"
        + "Doctor: " + doctor.getName() + "\n"
        + "Date: " + saved.getAppointmentDate(),
        "patient"
    );

    restTemplate.postForObject(
        "http://NOTIFICATION-SERVICE/notifications",
        notification,
        Void.class
    );

    return saved;
}

}