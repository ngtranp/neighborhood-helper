package org.usersrevice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.usersrevice.services.UserService;
import org.usersrevice.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(service.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        return service.login(username, password)
                .map(u -> ResponseEntity.ok("Login successful!"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        Optional<User> user = service.findByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/service-request")
    public ResponseEntity<?> createServiceRequest(
            @RequestParam String username,
            @RequestParam String password,
            @RequestBody Map<String, Object> requestDetails) {
        return service.createServiceRequest(username, password, requestDetails);
    }

    @PostMapping("/complete-service-request")
    public ResponseEntity<?> completeServiceRequest(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Long requestId) {
        return service.completeServiceRequest(username, password, requestId);
    }

    @GetMapping("/service-request-status")
    public ResponseEntity<?> getServiceRequestStatus(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Long requestId) {
        return service.getServiceRequestStatus(username, password, requestId);
    }

    @GetMapping("/all-service-requests")
    public ResponseEntity<?> getAllServiceRequests(){
        return service.listAllServiceRequests();
    }

    @PostMapping("/accept-service-request")
    public ResponseEntity<?> acceptServiceRequest(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Long requestId) {
        return service.acceptServiceRequest(username, password, requestId);
    }


}
