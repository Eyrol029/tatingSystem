package com.backend.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.LoginRequest;
import com.backend.backend.model.User;
import com.backend.backend.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class ClientController {

    @Autowired
    private UserService userService;
@CrossOrigin(origins = "*")
    @RequestMapping("/")
    public String hello_world() {
        return "ulol";
    }
  @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userService.addUser(user);
        return "User Added Successfully..";
    }
  @CrossOrigin(origins = "*")
    @RequestMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }
  @CrossOrigin(origins = "*")
    @RequestMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }
  @CrossOrigin(origins = "*")
    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
  @CrossOrigin(origins = "*")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "User Deleted";
    }
    // ✅ NEW - Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword(),
                loginRequest.getRole()
            );
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid email or password.");
        }
    }
}
