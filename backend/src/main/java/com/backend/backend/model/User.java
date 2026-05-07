
package com.backend.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UserAccount")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private long userID;

    @Column(name = "patientID")
    private Integer patientID;

    @Column(name = "employeeID")
    private Integer employeeID;

    // Login Credentials
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "passwordHash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "passwordSalt", length = 255)
    private String passwordSalt;

    @Column(name = "email", length = 200)
    private String email;

    // Access Control
    @Column(name = "role", nullable = false, length = 20)
    private String role; // 'Admin', 'Midwife', 'Patient'

    @Column(name = "status", length = 20)
    private String status = "Active"; // 'Active', 'Inactive'

    // Getters and Setters
    public long getUserID() { return userID; }
    public void setUserID(long userID) { this.userID = userID; }

    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }

    public Integer getEmployeeID() { return employeeID; }
    public void setEmployeeID(Integer employeeID) { this.employeeID = employeeID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordSalt() { return passwordSalt; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}