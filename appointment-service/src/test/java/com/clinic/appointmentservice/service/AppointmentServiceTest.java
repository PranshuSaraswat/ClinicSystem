package com.clinic.appointmentservice.service;

import com.clinic.appointmentservice.entity.Appointment;
import com.clinic.appointmentservice.model.DoctorDTO;
import com.clinic.appointmentservice.model.Notification;
import com.clinic.appointmentservice.model.PatientDTO;
import com.clinic.appointmentservice.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AppointmentService
 * Tests the orchestration logic for booking appointments
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Appointment Service Tests")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment sampleAppointment;
    private PatientDTO samplePatient;
    private DoctorDTO sampleDoctor;

    @BeforeEach
    void setUp() {
        // Setup sample appointment
        sampleAppointment = new Appointment();
        sampleAppointment.setPatientId(1L);
        sampleAppointment.setDoctorId(1L);
        sampleAppointment.setAppointmentDate(LocalDate.of(2025, 12, 15));

        // Setup sample patient
        samplePatient = new PatientDTO();
        samplePatient.setId(1L);
        samplePatient.setName("John Doe");
        samplePatient.setAge(30);
        samplePatient.setGender("Male");

        // Setup sample doctor
        sampleDoctor = new DoctorDTO();
        sampleDoctor.setId(1L);
        sampleDoctor.setName("Dr. Smith");
        sampleDoctor.setSpecialization("Cardiology");
        sampleDoctor.setAvailable(true);
    }

    @Test
    @DisplayName("Should successfully book appointment with valid patient and doctor")
    void testBookAppointment_Success() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setPatientId(1L);
        savedAppointment.setDoctorId(1L);
        savedAppointment.setAppointmentDate(LocalDate.of(2025, 12, 15));

        when(restTemplate.getForObject(
                eq("http://PATIENT-SERVICE/patients/1"),
                eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(
                eq("http://DOCTOR-SERVICE/doctors/1"),
                eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(savedAppointment);

        when(restTemplate.postForObject(
                anyString(),
                any(),
                eq(Object.class)))
                .thenReturn(new Object());

        when(restTemplate.postForObject(
                anyString(),
                any(Notification.class),
                eq(Void.class)))
                .thenReturn(null);

        // When
        Appointment result = appointmentService.bookAppointment(sampleAppointment);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPatientId());
        assertEquals(1L, result.getDoctorId());

        // Verify all service calls were made
        verify(restTemplate, times(1)).getForObject(
                eq("http://PATIENT-SERVICE/patients/1"),
                eq(PatientDTO.class));

        verify(restTemplate, times(1)).getForObject(
                eq("http://DOCTOR-SERVICE/doctors/1"),
                eq(DoctorDTO.class));

        verify(appointmentRepository, times(1)).save(any(Appointment.class));

        // Verify billing service was called
        verify(restTemplate, times(1)).postForObject(
                contains("http://BILLING-SERVICE/bills"),
                isNull(),
                eq(Object.class));

        // Verify notification service was called
        verify(restTemplate, times(1)).postForObject(
                eq("http://NOTIFICATION-SERVICE/notifications"),
                any(Notification.class),
                eq(Void.class));
    }

    @Test
    @DisplayName("Should include patient and doctor names in notification")
    void testBookAppointment_NotificationContent() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setPatientId(1L);
        savedAppointment.setDoctorId(1L);
        savedAppointment.setAppointmentDate(LocalDate.of(2025, 12, 15));

        when(restTemplate.getForObject(anyString(), eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(anyString(), eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(savedAppointment);

        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        // When
        appointmentService.bookAppointment(sampleAppointment);

        // Then
        verify(restTemplate).postForObject(
                eq("http://NOTIFICATION-SERVICE/notifications"),
                argThat(notification -> {
                    Notification n = (Notification) notification;
                    return n.getMessage().contains("John Doe") &&
                           n.getMessage().contains("Dr. Smith") &&
                           n.getMessage().contains("2025-12-15");
                }),
                eq(Void.class));
    }

    @Test
    @DisplayName("Should call billing service with correct appointment and patient IDs")
    void testBookAppointment_BillingServiceCall() {
        // Given
        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(5L);
        savedAppointment.setPatientId(3L);
        savedAppointment.setDoctorId(2L);
        savedAppointment.setAppointmentDate(LocalDate.now());

        when(restTemplate.getForObject(anyString(), eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(anyString(), eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(savedAppointment);

        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        // When
        appointmentService.bookAppointment(sampleAppointment);

        // Then
        verify(restTemplate).postForObject(
                eq("http://BILLING-SERVICE/bills?appointmentId=5&patientId=3"),
                isNull(),
                eq(Object.class));
    }

    @Test
    @DisplayName("Should validate patient exists before booking")
    void testBookAppointment_ValidatesPatient() {
        // Given
        when(restTemplate.getForObject(
                eq("http://PATIENT-SERVICE/patients/1"),
                eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(
                eq("http://DOCTOR-SERVICE/doctors/1"),
                eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(sampleAppointment);

        // When
        appointmentService.bookAppointment(sampleAppointment);

        // Then
        verify(restTemplate, times(1)).getForObject(
                eq("http://PATIENT-SERVICE/patients/1"),
                eq(PatientDTO.class));
    }

    @Test
    @DisplayName("Should validate doctor exists before booking")
    void testBookAppointment_ValidatesDoctor() {
        // Given
        when(restTemplate.getForObject(
                eq("http://PATIENT-SERVICE/patients/1"),
                eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(
                eq("http://DOCTOR-SERVICE/doctors/1"),
                eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(sampleAppointment);

        // When
        appointmentService.bookAppointment(sampleAppointment);

        // Then
        verify(restTemplate, times(1)).getForObject(
                eq("http://DOCTOR-SERVICE/doctors/1"),
                eq(DoctorDTO.class));
    }

    @Test
    @DisplayName("Should save appointment to repository")
    void testBookAppointment_SavesAppointment() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(anyString(), eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(sampleAppointment);

        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        // When
        appointmentService.bookAppointment(sampleAppointment);

        // Then
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should handle appointment with future date")
    void testBookAppointment_FutureDate() {
        // Given
        Appointment futureAppointment = new Appointment();
        futureAppointment.setPatientId(1L);
        futureAppointment.setDoctorId(1L);
        futureAppointment.setAppointmentDate(LocalDate.now().plusDays(7));

        Appointment savedAppointment = new Appointment();
        savedAppointment.setId(1L);
        savedAppointment.setPatientId(1L);
        savedAppointment.setDoctorId(1L);
        savedAppointment.setAppointmentDate(LocalDate.now().plusDays(7));

        when(restTemplate.getForObject(anyString(), eq(PatientDTO.class)))
                .thenReturn(samplePatient);

        when(restTemplate.getForObject(anyString(), eq(DoctorDTO.class)))
                .thenReturn(sampleDoctor);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(savedAppointment);

        when(restTemplate.postForObject(anyString(), any(), eq(Object.class)))
                .thenReturn(new Object());

        // When
        Appointment result = appointmentService.bookAppointment(futureAppointment);

        // Then
        assertNotNull(result);
        assertTrue(result.getAppointmentDate().isAfter(LocalDate.now()));
    }
}
