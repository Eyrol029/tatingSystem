package com.backend.backend.service.Appointment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Appointment.Appointment;
import com.backend.backend.repository.Appointment.AppointmentRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Override
    public Appointment add(Appointment appointment) {
        return repository.save(appointment);
    }

    @Override
    public Appointment getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
    }

    @Override
    public List<Appointment> getAll() {
        return repository.findAll();
    }

    @Override
    public Appointment update(Appointment appointment) {
        return repository.save(appointment);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
