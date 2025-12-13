package com.clinic.appointmentservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.repository.AppointmentRepository;
import com.clinic.appointmentservice.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;
    private final AppointmentRepository repo;

    public AppointmentController(AppointmentService service,
                                 AppointmentRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    public Appointment book(@RequestBody Appointment appointment) {
        return service.bookAppointment(appointment);
    }

    // READ ALL
    @GetMapping
    public List<Appointment> getAll() {
        return repo.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Appointment getOne(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
