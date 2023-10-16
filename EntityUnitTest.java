package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.type.TrueFalseType;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    //Tests for Person
    @Test
    void person_shouldMatchInfo(){
        Person person1 = new Person("Samuel","Santillana",22,"s.santillana@hospital.accwe");

        Assertions.assertEquals("Samuel", person1.getFirstName());
        Assertions.assertEquals("Santillana",person1.getLastName());
        Assertions.assertEquals(22,person1.getAge());
        Assertions.assertEquals("s.santillana@hospital.accwe",person1.getEmail());
    }
    // Tests for Patient
    @Test
    void patient_shouldGetIdAndSetId(){
        Patient patient1 = new Patient("Samuel","Santillana",22,"samuel.s@hospital.accwe");
        long ID = 15;
        patient1.setId(ID);

        Assertions.assertEquals(ID,patient1.getId());
    }
    // Test for Room
    void room_shouldGetRoomName(){
        Room room1 = new Room("Dermatology");

        Assertions.assertEquals("Dermatology",room1.getRoomName());
    }
    // Test for Doctor
    @Test
    void doctor_shouldGetIdAndSetId(){
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        long ID = 12;
        doctor1.setId(12);

        Assertions.assertEquals(doctor1.getId(),12);
    }
    @Test
    void appointment_shouldMatchInfo(){
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Assertions.assertEquals(doctor1,appointment.getDoctor());
        Assertions.assertEquals(patient1,appointment.getPatient());
        Assertions.assertEquals(room1,appointment.getRoom());
        Assertions.assertEquals(startsAt,appointment.getStartsAt());
        Assertions.assertEquals(finishesAt,appointment.getFinishesAt());
    }
    @Test
    void appointment_shouldSetInfo(){
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"paco.jose@hospitla.accwe");
        Room new_room = new Room("Pediatry");
        LocalDateTime new_startsAt = LocalDateTime.parse("11:30 16/10/2023", formatter);
        LocalDateTime new_finishesAt = LocalDateTime.parse("11:45 16/10/2023", formatter);

        appointment.setDoctor(doctor2);
        appointment.setPatient(patient2);
        appointment.setRoom(new_room);
        appointment.setStartsAt(new_startsAt);
        appointment.setFinishesAt(new_finishesAt);

        Assertions.assertEquals(doctor2,appointment.getDoctor());
        Assertions.assertEquals(patient2,appointment.getPatient());
        Assertions.assertEquals(new_room,appointment.getRoom());
        Assertions.assertEquals(new_startsAt,appointment.getStartsAt());
        Assertions.assertEquals(new_finishesAt,appointment.getFinishesAt());
    }
    @Test
    void shouldNotOverlap_DifferentRooms(){
        // This is the case where the rooms are different
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Pediatry");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:35 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertFalse(appointment1.overlaps(appointment2));
    }
    @Test
    void shouldOverlapCase3(){
        // Case 3: A.starts < B.finishes && B.finishes < A.finishes
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:35 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertTrue(appointment1.overlaps(appointment2));
    }
    @Test
    void shouldOverlapCase4(){
        // Case 4: B.starts < A.starts && A.finishes < B.finishes
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:40 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertTrue(appointment1.overlaps(appointment2));
    }
    @Test
    void shouldOverlapCase1(){
        // Case 1: A.starts == B.starts
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:45 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:30 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertTrue(appointment1.overlaps(appointment2));
    }
    @Test
    void shouldOverlapCase2(){
        // Case 1: A.starts == B.starts
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:45 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:45 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:10 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:45 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertTrue(appointment1.overlaps(appointment2));
    }
    @Test
    void shouldNotOverlap_SameRoom(){
        // Case 5: Same room and case different from 1 to 4.
        Doctor doctor1 = new Doctor("Samuel","Santillana",24,"s.santill@hospital.accwe");
        Patient patient1 = new Patient("Pedro","Jimenez",31,"p.jimenez@hospital.accwe");
        Room room1 = new Room("Dermatology");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        LocalDateTime startsAt= LocalDateTime.parse("19:00 16/10/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:30 16/10/2023", formatter);
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt, finishesAt);

        Doctor doctor2 = new Doctor("Jose","Hernandez",45,"jose.hernan@hospitla.accwe");
        Patient patient2 = new Patient("Paco","Jose",12,"p.jose@hospitla.accwe");
        Room room2 = new Room("Dermatology");
        LocalDateTime startsAt2 = LocalDateTime.parse("19:31 16/10/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:00 16/10/2023", formatter);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);

        Assertions.assertFalse(appointment1.overlaps(appointment2));
    }
}
