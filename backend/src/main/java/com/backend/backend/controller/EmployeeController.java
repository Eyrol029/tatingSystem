package com.backend.backend.controller;

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

import com.backend.backend.model.Employee;
import com.backend.backend.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @PutMapping
    public Employee updateEmployee(@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted.";
    }

    @GetMapping("/search/department/{department}")
    public List<Employee> getByDepartment(@PathVariable String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    @GetMapping("/search/status/{status}")
    public List<Employee> getByStatus(@PathVariable String status) {
        return employeeService.getEmployeesByStatus(status);
    }

    @GetMapping("/search/position/{position}")
    public List<Employee> getByPosition(@PathVariable String position) {
        return employeeService.getEmployeesByPosition(position);
    }
}
