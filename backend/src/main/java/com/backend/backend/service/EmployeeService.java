package com.backend.backend.service;

import java.util.List;

import com.backend.backend.model.Employee;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    Employee getEmployeeById(Integer id);

    List<Employee> getEmployees();

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Integer id);

    List<Employee> getEmployeesByDepartment(String department);

    List<Employee> getEmployeesByStatus(String status);

    List<Employee> getEmployeesByPosition(String position);
}
