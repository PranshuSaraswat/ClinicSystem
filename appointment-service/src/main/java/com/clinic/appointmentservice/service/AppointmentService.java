package com.clinic.appointmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.repository.AppointmentRepository;

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

        return repo.save(appointment);
    }
}
