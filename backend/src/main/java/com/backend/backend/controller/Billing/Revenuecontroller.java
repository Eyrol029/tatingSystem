package com.backend.backend.controller.Billing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Billing.Revenue;
import com.backend.backend.service.Billing.Revenueservice;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/revenue")
public class Revenuecontroller {

    @Autowired
    private Revenueservice service;

    @GetMapping
    public List<Revenue> getAll() {
        return service.getAllRevenue();
    }

    @GetMapping("/{id}")
    public Revenue getById(@PathVariable Integer id) {
        return service.getRevenueById(id);
    }

    @PostMapping
    public Revenue add(@RequestBody Revenue revenue) {
        return service.addRevenue(revenue);
    }

    @PutMapping
    public Revenue update(@RequestBody Revenue revenue) {
        return service.updateRevenue(revenue);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteRevenue(id);
        return "Revenue deleted successfully.";
    }
}
