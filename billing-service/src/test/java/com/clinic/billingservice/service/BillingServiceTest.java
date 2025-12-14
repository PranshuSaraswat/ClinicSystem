package com.clinic.billingservice.service;

import com.clinic.billingservice.entity.Bill;
import com.clinic.billingservice.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BillingService
 * Tests billing logic and payment status management
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Billing Service Tests")
class BillingServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillingService billingService;

    private Bill sampleBill;

    @BeforeEach
    void setUp() {
        sampleBill = new Bill();
        sampleBill.setId(1L);
        sampleBill.setAppointmentId(1L);
        sampleBill.setPatientId(1L);
        sampleBill.setAmount(100.0);
        sampleBill.setStatus("UNPAID");
    }

    @Test
    @DisplayName("Should create bill with default amount and UNPAID status")
    void testCreateBill() {
        // Given
        Long appointmentId = 1L;
        Long patientId = 1L;

        when(billRepository.save(any(Bill.class))).thenReturn(sampleBill);

        // When
        Bill result = billingService.createBill(appointmentId, patientId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getAppointmentId());
        assertEquals(1L, result.getPatientId());
        assertEquals(100.0, result.getAmount());
        assertEquals("UNPAID", result.getStatus());

        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should retrieve bill by ID")
    void testGetBillById() {
        // Given
        when(billRepository.findById(1L)).thenReturn(Optional.of(sampleBill));

        // When
        Bill result = billingService.getBillById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UNPAID", result.getStatus());

        verify(billRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when bill not found")
    void testGetBillById_NotFound() {
        // Given
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            billingService.getBillById(999L);
        });

        verify(billRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should retrieve all bills for a patient")
    void testGetBillsByPatient() {
        // Given
        Bill bill2 = new Bill();
        bill2.setId(2L);
        bill2.setAppointmentId(2L);
        bill2.setPatientId(1L);
        bill2.setAmount(150.0);
        bill2.setStatus("PAID");

        List<Bill> patientBills = Arrays.asList(sampleBill, bill2);
        when(billRepository.findByPatientId(1L)).thenReturn(patientBills);

        // When
        List<Bill> result = billingService.getBillsByPatient(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPatientId());
        assertEquals(1L, result.get(1).getPatientId());

        verify(billRepository, times(1)).findByPatientId(1L);
    }

    @Test
    @DisplayName("Should return empty list when patient has no bills")
    void testGetBillsByPatient_NoBills() {
        // Given
        when(billRepository.findByPatientId(anyLong())).thenReturn(Arrays.asList());

        // When
        List<Bill> result = billingService.getBillsByPatient(999L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(billRepository, times(1)).findByPatientId(999L);
    }

    @Test
    @DisplayName("Should mark bill as PAID")
    void testMarkAsPaid() {
        // Given
        when(billRepository.findById(1L)).thenReturn(Optional.of(sampleBill));

        Bill paidBill = new Bill();
        paidBill.setId(1L);
        paidBill.setAppointmentId(1L);
        paidBill.setPatientId(1L);
        paidBill.setAmount(100.0);
        paidBill.setStatus("PAID");

        when(billRepository.save(any(Bill.class))).thenReturn(paidBill);

        // When
        Bill result = billingService.markAsPaid(1L);

        // Then
        assertNotNull(result);
        assertEquals("PAID", result.getStatus());

        verify(billRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should throw exception when marking non-existent bill as paid")
    void testMarkAsPaid_NotFound() {
        // Given
        when(billRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            billingService.markAsPaid(999L);
        });

        verify(billRepository, times(1)).findById(999L);
        verify(billRepository, never()).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should delete bill")
    void testDeleteBill() {
        // Given
        doNothing().when(billRepository).deleteById(1L);

        // When
        billingService.deleteBill(1L);

        // Then
        verify(billRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should create multiple bills for same patient")
    void testCreateBill_MultipleForSamePatient() {
        // Given
        Bill bill1 = new Bill();
        bill1.setId(1L);
        bill1.setAppointmentId(1L);
        bill1.setPatientId(1L);
        bill1.setAmount(100.0);
        bill1.setStatus("UNPAID");

        Bill bill2 = new Bill();
        bill2.setId(2L);
        bill2.setAppointmentId(2L);
        bill2.setPatientId(1L);
        bill2.setAmount(150.0);
        bill2.setStatus("UNPAID");

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill1)
                .thenReturn(bill2);

        // When
        Bill result1 = billingService.createBill(1L, 1L);
        Bill result2 = billingService.createBill(2L, 1L);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1L, result1.getPatientId());
        assertEquals(1L, result2.getPatientId());
        assertNotEquals(result1.getId(), result2.getId());

        verify(billRepository, times(2)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should handle different bill amounts")
    void testCreateBill_DifferentAmounts() {
        // Given
        Bill expensiveBill = new Bill();
        expensiveBill.setId(1L);
        expensiveBill.setAppointmentId(1L);
        expensiveBill.setPatientId(1L);
        expensiveBill.setAmount(500.0);
        expensiveBill.setStatus("UNPAID");

        when(billRepository.save(any(Bill.class))).thenReturn(expensiveBill);

        // When
        Bill result = billingService.createBill(1L, 1L);

        // Then
        assertNotNull(result);
        assertEquals(500.0, result.getAmount());
    }

    @Test
    @DisplayName("Should not change status when marking already paid bill")
    void testMarkAsPaid_AlreadyPaid() {
        // Given
        Bill alreadyPaidBill = new Bill();
        alreadyPaidBill.setId(1L);
        alreadyPaidBill.setAppointmentId(1L);
        alreadyPaidBill.setPatientId(1L);
        alreadyPaidBill.setAmount(100.0);
        alreadyPaidBill.setStatus("PAID");

        when(billRepository.findById(1L)).thenReturn(Optional.of(alreadyPaidBill));
        when(billRepository.save(any(Bill.class))).thenReturn(alreadyPaidBill);

        // When
        Bill result = billingService.markAsPaid(1L);

        // Then
        assertNotNull(result);
        assertEquals("PAID", result.getStatus());

        verify(billRepository, times(1)).save(any(Bill.class));
    }
}
