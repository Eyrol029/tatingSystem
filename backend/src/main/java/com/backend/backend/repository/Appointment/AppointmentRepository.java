package com.backend.backend.repository.Appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.Appointment.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByPatientID(Integer patientID);

    void deleteByPatientID(Integer patientID);
}
