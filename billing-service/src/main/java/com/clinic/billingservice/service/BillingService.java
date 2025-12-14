package com.clinic.billingservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.billingservice.entity.Bill;
import com.clinic.billingservice.repository.BillRepository;

@Service
public class BillingService {

    private final BillRepository repo;

    public BillingService(BillRepository repo) {
        this.repo = repo;
    }

    public Bill createBill(Long appointmentId, Long patientId) {
        Bill bill = new Bill();
        bill.setAppointmentId(appointmentId);
        bill.setPatientId(patientId);
        bill.setAmount(500.0);
        bill.setStatus("UNPAID");
        return repo.save(bill);
    }

    public Bill getBillById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    public List<Bill> getBillsByPatient(Long patientId) {
        return repo.findByPatientId(patientId);
    }

    public Bill markAsPaid(Long id) {
        Bill bill = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        bill.setStatus("PAID");
        return repo.save(bill);
    }

    public void deleteBill(Long id) {
        repo.deleteById(id);
    }
}
