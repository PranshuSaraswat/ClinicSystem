package com.clinic.billingservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.clinic.billingservice.entity.Bill;
import com.clinic.billingservice.service.BillingService;

@RestController
@RequestMapping("/bills")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    // 1️⃣ Create Bill
    @PostMapping
    public Bill createBill(
            @RequestParam Long appointmentId,
            @RequestParam Long patientId) {
        return service.createBill(appointmentId, patientId);
    }

    // 2️⃣ Get Bill by ID
    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Long id) {
        return service.getBillById(id);
    }

    // 3️⃣ Get Bills by Patient
    @GetMapping("/patient/{patientId}")
    public List<Bill> getBillsByPatient(@PathVariable Long patientId) {
        return service.getBillsByPatient(patientId);
    }

    // 4️⃣ Mark Bill as PAID
    @PutMapping("/{id}/pay")
    public Bill payBill(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    // 5️⃣ Delete Bill
    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        service.deleteBill(id);
    }
}
