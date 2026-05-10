package com.backend.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    List<Employee> findAll();

    List<Employee> findByDepartment(String department);

    List<Employee> findByStatus(String status);

    List<Employee> findByPosition(String position);
}
