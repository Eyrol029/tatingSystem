package com.backend.backend.controller.Appointment;

import com.backend.backend.model.Appointment.Appointment;
import com.backend.backend.service.Appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    // CREATE
    @PostMapping
    public Appointment add(@RequestBody Appointment appointment) {
        return service.add(appointment);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // GET ALL
    @GetMapping
    public List<Appointment> getAll() {
        return service.getAll();
    }

    // UPDATE
    @PutMapping
    public Appointment update(@RequestBody Appointment appointment) {
        return service.update(appointment);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Appointment deleted successfully.";
    }
}