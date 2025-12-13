package com.clinic.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.appointmentservice.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
