package com.backend.backend.service;

import java.util.List;

import com.backend.backend.model.User;

public interface UserService {

    User addUser(User user);

    User getUserById(long id);

    List<User> getUsers();

    void deleteUser(long id);

    User updateUser(User user);

    // Additional methods for new fields
    User getUserByUsername(String username);

    List<User> getUsersByRole(String role);

    List<User> getUsersByStatus(String status);

    User getUserByEmail(String email);

    List<User> getUsersByPatientID(Integer patientID);

    List<User> getUsersByEmployeeID(Integer employeeID);
    
    User login(String email, String password, String role);
}