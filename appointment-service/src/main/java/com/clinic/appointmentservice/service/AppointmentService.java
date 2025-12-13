package com.clinic.appointmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.repository.AppointmentRepository;
import com.clinic.appointmentservice.model.Notification;


@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final RestTemplate restTemplate;

    public AppointmentService(AppointmentRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public Appointment bookAppointment(Appointment appointment) {

    // Check patient exists
        restTemplate.getForObject(
            "http://PATIENT-SERVICE/patients/" + appointment.getPatientId(),
            Object.class
        );

    // Check doctor exists
        restTemplate.getForObject(
            "http://DOCTOR-SERVICE/doctors/" + appointment.getDoctorId(),
            Object.class
        );

    // Save appointment
        Appointment saved = repo.save(appointment);

    // Send notification
        Notification notification = new Notification(
            "Appointment booked successfully",
            "Patient ID: " + appointment.getPatientId()
        );

        restTemplate.postForObject(
            "http://NOTIFICATION-SERVICE/notifications",
            notification,
        Void.class
        );

        return saved;
    }

}
