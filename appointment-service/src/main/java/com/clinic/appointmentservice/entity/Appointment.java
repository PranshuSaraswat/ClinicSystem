package com.clinic.appointmentservice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long doctorId;
    private Long patientId;
    private LocalDate appointmentDate;

    public Appointment() {}

    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public Long getPatientId() { return patientId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }

    public void setId(Long id) { this.id = id; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
