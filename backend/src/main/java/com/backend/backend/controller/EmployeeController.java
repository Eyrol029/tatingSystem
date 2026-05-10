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
    @CrossOrigin(origins = "*")
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }
     @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }
     @CrossOrigin(origins = "*")
    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }
     @CrossOrigin(origins = "*")
    @PutMapping
    public Employee updateEmployee(@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }
     @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return "Employee deleted.";
    }
     @CrossOrigin(origins = "*")
    @GetMapping("/search/department/{department}")
    public List<Employee> getByDepartment(@PathVariable String department) {
        return employeeService.getEmployeesByDepartment(department);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/search/status/{status}")
    public List<Employee> getByStatus(@PathVariable String status) {
        return employeeService.getEmployeesByStatus(status);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/search/position/{position}")
    public List<Employee> getByPosition(@PathVariable String position) {
        return employeeService.getEmployeesByPosition(position);
    }
}
