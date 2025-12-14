package com.clinic.billingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.clinic.billingservice.entity.Bill;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatientId(Long patientId);
}
