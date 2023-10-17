
package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;



/** TODO
 * Implement all the unit test in its corresponding class.
 * Make sure to be as exhaustive as possible. Coverage is checked ;)
 */

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void shouldGetNoDoctors() throws Exception{
        List<Doctor> doctorsList = new ArrayList<Doctor>();
        when(doctorRepository.findAll()).thenReturn(doctorsList);
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());

    }
    @Test
    void shouldGetAllDoctors() throws Exception{
        Doctor doctor = new Doctor ("Pedro", "Fernandez", 31, "pedro.hernandez@hospital.accwe");
        Doctor doctor2 = new Doctor ("Manuel", "Jordan", 21, "manuel.jordan@hospital.accwe");

        ArrayList<Doctor> listDoctors = new ArrayList<Doctor>();

        listDoctors.add(doctor);
        listDoctors.add(doctor2);

        when(doctorRepository.findAll()).thenReturn(listDoctors);
        mockMvc.perform(get("/api/doctors")).andExpect(status().isOk());
    }
    @Test
    void shouldNotGetAnyDoctorById() throws Exception{
        long id = 14;
        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }
    @Test
    void shouldGetDoctorByID() throws Exception{
        Doctor doctor = new Doctor("Samuel", "Fernandez", 25, "samuel.hernandez@hospital.accwe");
        int ID = 1;

        doctor.setId(ID);

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(opt.get().getId()).isEqualTo(ID);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateDoctor() throws Exception{
        Doctor doctor = new Doctor("Celia", "Manrique", 52, "celia.m@hospital.accwe");
        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldDeleteDoctorByID() throws Exception{
        Doctor doctor = new Doctor("Samuel", "Fernandez", 25, "samuel.hernandez@hospital.accwe");
        long ID = 1;

        doctor.setId(ID);

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(opt.get().getId()).isEqualTo(ID);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldNotDeleteAnyDoctorById() throws Exception{
        long id = 14;
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }
    @Test
    void shouldDeleteAllAppointments() throws Exception{
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetNoPatients() throws Exception{
        List<Patient> patientList = new ArrayList<Patient>();
        when(patientRepository.findAll()).thenReturn(patientList);
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldGetAllPatients() throws Exception{

        Patient patient = new Patient("Alvaro", "Gomez", 21, "a.gomez@hospital.accwe");
        List<Patient> patientList = new ArrayList<Patient>();
        patientList.add(patient);

        when(patientRepository.findAll()).thenReturn(patientList);
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldGetPatientById() throws Exception{
        Patient patient = new Patient("Alvaro", "Gomez", 21, "a.gomez@hospital.accwe");

        long ID = 1;
        patient.setId(ID);

        Optional<Patient> opt =  Optional.of(patient);

        assertThat(opt.isPresent());
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(opt.get().getId()).isEqualTo(ID);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());

    }
    @Test
    void shouldNotFoundPatientById() throws Exception{
        long ID = 15;
        mockMvc.perform(get("/api/patients/" + ID)).andExpect(status().isNotFound());
    }
    @Test
    void shouldCreatePatient() throws Exception{
        Patient patient = new Patient("Paco", "Sanchez", 81, "p.sanchez@email.com");

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldNotDeletePatientById() throws Exception{
        long ID = 14;
        mockMvc.perform(delete("/api/patient/" + ID)).andExpect(status().isNotFound());
    }
    @Test
    void shouldDeletePatientById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Sanchez", 31, "j.sanchez@email.com");

        long ID = 1;
        patient.setId(ID);

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(ID);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldDeleteAllPatients() throws Exception{
        mockMvc.perform(delete("/api/patients")).andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllRooms() throws Exception{
        Room room1 = new Room("Dermatology");

        ArrayList<Room> allRooms = new ArrayList<Room>();
        allRooms.add(room1);

        when(roomRepository.findAll()).thenReturn(allRooms);
        mockMvc.perform(get("/api/rooms")).andExpect(status().isOk());
    }
    @Test
    void shouldNotGetAnyRoom() throws Exception{
        ArrayList<Room> allRooms = new ArrayList<Room>();
        when(roomRepository.findAll()).thenReturn(allRooms);

        mockMvc.perform(get("/api/rooms")).andExpect(status().isNoContent());
    }
    @Test
    void shouldGetRoomById() throws Exception{
        String roomName = "Dermatology";
        Room room1 = new Room(roomName);

        Optional<Room> opt = Optional.of(room1);

        assertThat(opt.isPresent());
        assertThat(opt.get().getRoomName()).isEqualTo(room1.getRoomName());
        assertThat(opt.get().getRoomName()).isEqualTo(roomName);

        when(roomRepository.findByRoomName(roomName)).thenReturn(opt);
        mockMvc.perform(get("/api/rooms/" + room1.getRoomName())).andExpect(status().isOk());
    }
    @Test
    void shouldNotGetRoomById() throws Exception{
        String roomName = "Dermatology";

        mockMvc.perform(get("/api/rooms/" + roomName)).andExpect(status().isNotFound());

    }
    @Test
    void shouldCreateRoom() throws Exception {
        Room room = new Room("Pediatry");

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
    }
    @Test
    void shouldNotDeleteRoom() throws Exception{
        String roomName = "Dermatology";

        mockMvc.perform(delete("/api/rooms/" + roomName)).andExpect(status().isNotFound());
    }
    @Test
    void shouldDeleteRoom() throws Exception{
        String roomName = "Pediatry";
        Room room = new Room(roomName);

        Optional<Room> opt = Optional.of(room);

        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);
        mockMvc.perform(delete("/api/rooms/" + room.getRoomName()))
                .andExpect(status().isOk());
    }
    @Test
    void shouldDeleteAllRooms() throws Exception{
        mockMvc.perform(delete("/api/rooms")).andExpect(status().isOk());
    }

}
