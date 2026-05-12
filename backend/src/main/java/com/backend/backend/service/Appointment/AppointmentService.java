package com.backend.backend.service.Appointment;

import java.util.List;

import com.backend.backend.model.Appointment.Appointment;

public interface AppointmentService {

    Appointment add(Appointment appointment);

    Appointment getById(Integer id);

    List<Appointment> getAll();

    Appointment update(Appointment appointment);

    void delete(Integer id);
}
