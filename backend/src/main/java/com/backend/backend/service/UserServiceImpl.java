package com.backend.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.User;
import com.backend.backend.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id)); // ✅ fixed warning
    }

    @Override
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public List<User> getUsersByPatientID(Integer patientID) {
        return userRepository.findByPatientID(patientID);
    }

    @Override
    public List<User> getUsersByEmployeeID(Integer employeeID) {
        return userRepository.findByEmployeeID(employeeID);
    }

    @Override
    public User login(String email, String password, String role) {
    // Find user by email and role
    User user = userRepository.findByEmailAndRole(email, role)
        .orElseThrow(() -> new RuntimeException("Invalid email or password."));

    // Check password
    if (!user.getPasswordHash().equals(password)) {
        throw new RuntimeException("Invalid email or password.");
    }

    // Check if account is active
    if (!user.getStatus().equals("Active")) {
        throw new RuntimeException("Account is inactive.");
    }

    return user;
}
}