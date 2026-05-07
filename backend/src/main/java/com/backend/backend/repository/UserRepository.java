package com.backend.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    List<User> findByStatus(String status);

    List<User> findByPatientID(Integer patientID);

    List<User> findByEmployeeID(Integer employeeID);

      Optional<User> findByEmailAndRole(String email, String role); 
}