package com.clinic.doctorservice.controller;

import com.clinic.doctorservice.entity.Doctor;
import com.clinic.doctorservice.repository.DoctorRepository;
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
 * Unit tests for DoctorController
 * Tests all CRUD operations for the Doctor REST API
 */
@WebMvcTest(DoctorController.class)
@DisplayName("Doctor Controller Tests")
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Doctor sampleDoctor;

    @BeforeEach
    void setUp() {
        sampleDoctor = new Doctor();
        sampleDoctor.setId(1L);
        sampleDoctor.setName("Dr. Smith");
        sampleDoctor.setSpecialization("Cardiology");
        sampleDoctor.setAvailable(true);
    }

    @Test
    @DisplayName("Should create a new doctor")
    void testAddDoctor() throws Exception {
        // Given
        Doctor newDoctor = new Doctor();
        newDoctor.setName("Dr. Johnson");
        newDoctor.setSpecialization("Neurology");
        newDoctor.setAvailable(true);

        when(doctorRepository.save(any(Doctor.class))).thenReturn(sampleDoctor);

        // When & Then
        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDoctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Smith"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"))
                .andExpect(jsonPath("$.available").value(true));

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should return all doctors")
    void testGetAllDoctors() throws Exception {
        // Given
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. Williams");
        doctor2.setSpecialization("Orthopedics");
        doctor2.setAvailable(false);

        List<Doctor> doctors = Arrays.asList(sampleDoctor, doctor2);
        when(doctorRepository.findAll()).thenReturn(doctors);

        // When & Then
        mockMvc.perform(get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Dr. Smith"))
                .andExpect(jsonPath("$[1].name").value("Dr. Williams"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].available").value(false));

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no doctors exist")
    void testGetAllDoctors_EmptyList() throws Exception {
        // Given
        when(doctorRepository.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return doctor by ID")
    void testGetDoctorById() throws Exception {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        // When & Then
        mockMvc.perform(get("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Smith"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"));

        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when doctor not found")
    void testGetDoctorById_NotFound() throws Exception {
        // Given
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/doctors/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(doctorRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update existing doctor")
    void testUpdateDoctor() throws Exception {
        // Given
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setName("Dr. Smith Updated");
        updatedDoctor.setSpecialization("Cardiology & Surgery");
        updatedDoctor.setAvailable(false);

        Doctor savedDoctor = new Doctor();
        savedDoctor.setId(1L);
        savedDoctor.setName("Dr. Smith Updated");
        savedDoctor.setSpecialization("Cardiology & Surgery");
        savedDoctor.setAvailable(false);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        // When & Then
        mockMvc.perform(put("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDoctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Smith Updated"))
                .andExpect(jsonPath("$.specialization").value("Cardiology & Surgery"))
                .andExpect(jsonPath("$.available").value(false));

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent doctor")
    void testUpdateDoctor_NotFound() throws Exception {
        // Given
        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setName("Dr. Smith Updated");
        updatedDoctor.setSpecialization("Cardiology");
        updatedDoctor.setAvailable(true);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/doctors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDoctor)))
                .andExpect(status().isInternalServerError());

        verify(doctorRepository, times(1)).findById(999L);
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should delete doctor by ID")
    void testDeleteDoctor() throws Exception {
        // Given
        doNothing().when(doctorRepository).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(doctorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle multiple doctors with same specialization")
    void testGetAllDoctors_SameSpecialization() throws Exception {
        // Given
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. Jones");
        doctor2.setSpecialization("Cardiology");
        doctor2.setAvailable(true);

        List<Doctor> cardiologists = Arrays.asList(sampleDoctor, doctor2);
        when(doctorRepository.findAll()).thenReturn(cardiologists);

        // When & Then
        mockMvc.perform(get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].specialization").value("Cardiology"))
                .andExpect(jsonPath("$[1].specialization").value("Cardiology"));

        verify(doctorRepository, times(1)).findAll();
    }
}
