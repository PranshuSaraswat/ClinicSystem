package com.clinic.patientservice.controller;

import com.clinic.patientservice.entity.Patient;
import com.clinic.patientservice.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for PatientController
 * Tests all CRUD operations for the Patient REST API
 */
@WebMvcTest(PatientController.class)
@DisplayName("Patient Controller Tests")
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient samplePatient;

    @BeforeEach
    void setUp() {
        samplePatient = new Patient();
        samplePatient.setId(1L);
        samplePatient.setName("John Doe");
        samplePatient.setAge(30);
        samplePatient.setGender("Male");
    }

    @Test
    @DisplayName("Should create a new patient")
    void testAddPatient() throws Exception {
        // Given
        Patient newPatient = new Patient();
        newPatient.setName("Jane Smith");
        newPatient.setAge(25);
        newPatient.setGender("Female");

        when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should return all patients")
    void testGetAllPatients() throws Exception {
        // Given
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Doe");
        patient2.setAge(28);
        patient2.setGender("Female");

        List<Patient> patients = Arrays.asList(samplePatient, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[1].age").value(28));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no patients exist")
    void testGetAllPatients_EmptyList() throws Exception {
        // Given
        when(patientRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return patient by ID")
    void testGetPatientById() throws Exception {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));

        // When & Then
        mockMvc.perform(get("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when patient not found")
    void testGetPatientById_NotFound() throws Exception {
        // Given
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/patients/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(patientRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update existing patient")
    void testUpdatePatient() throws Exception {
        // Given
        Patient updatedPatient = new Patient();
        updatedPatient.setName("John Doe Updated");
        updatedPatient.setAge(31);
        updatedPatient.setGender("Male");

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setName("John Doe Updated");
        savedPatient.setAge(31);
        savedPatient.setGender("Male");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // When & Then
        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.age").value(31))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(patientRepository, times(1)).findById(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent patient")
    void testUpdatePatient_NotFound() throws Exception {
        // Given
        Patient updatedPatient = new Patient();
        updatedPatient.setName("John Doe");
        updatedPatient.setAge(30);
        updatedPatient.setGender("Male");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/patients/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isInternalServerError());

        verify(patientRepository, times(1)).findById(999L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should delete patient by ID")
    void testDeletePatient() throws Exception {
        // Given
        doNothing().when(patientRepository).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle patients with different genders")
    void testGetAllPatients_DifferentGenders() throws Exception {
        // Given
        Patient male = new Patient();
        male.setId(1L);
        male.setName("John");
        male.setAge(30);
        male.setGender("Male");

        Patient female = new Patient();
        female.setId(2L);
        female.setName("Jane");
        female.setAge(25);
        female.setGender("Female");

        Patient other = new Patient();
        other.setId(3L);
        other.setName("Alex");
        other.setAge(28);
        other.setGender("Other");

        List<Patient> patients = Arrays.asList(male, female, other);
        when(patientRepository.findAll()).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].gender").value("Male"))
                .andExpect(jsonPath("$[1].gender").value("Female"))
                .andExpect(jsonPath("$[2].gender").value("Other"));

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle patients with minimum age")
    void testAddPatient_MinimumAge() throws Exception {
        // Given
        Patient youngPatient = new Patient();
        youngPatient.setName("Baby Doe");
        youngPatient.setAge(0);
        youngPatient.setGender("Male");

        Patient savedPatient = new Patient();
        savedPatient.setId(1L);
        savedPatient.setName("Baby Doe");
        savedPatient.setAge(0);
        savedPatient.setGender("Male");

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(youngPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(0));

        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}
