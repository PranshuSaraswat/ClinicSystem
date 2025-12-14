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

        Notification notification = new Notification(
            "Appointment booked successfully.\n\n"
            + "Patient: " + patient.getName() + " (ID: " + patient.getId() + ")\n"
            + "Doctor: " + doctor.getName() + " (ID: " + doctor.getId() + ")\n"
            + "Appointment Date: " + appointment.getAppointmentDate(),
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