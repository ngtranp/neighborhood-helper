package org.usersrevice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.usersrevice.entity.User;
import org.usersrevice.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String CORE_SERVICE_URL = "http://core-service:8082/api/core";

    public User registerUser(User user) {
        return repository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public ResponseEntity<?> createServiceRequest(String username, String password, Map<String, Object> requestDetails) {

        Optional<User> userOpt = findByUsernameAndPassword(username, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOpt.get();

        if (!"requester".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only users with the 'requester' role can create service requests");
        }

        if (!requestDetails.containsKey("description") || requestDetails.get("description") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Description is required");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("description", requestDetails.get("description"));
        payload.put("requesterEmail", requestDetails.get("requesterEmail"));
        payload.put("requesterId", user.getId());
        payload.put("status", "NEW");

        try {
            ResponseEntity<?> response = restTemplate.postForEntity(CORE_SERVICE_URL, payload, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Core Service: " + e.getMessage());
        }
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return repository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public ResponseEntity<?> listAllServiceRequests() {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(CORE_SERVICE_URL, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching service requests: " + e.getMessage());
        }
    }

    public ResponseEntity<?> completeServiceRequest(String username, String password, Long requestId) {
        Optional<User> userOpt = findByUsernameAndPassword(username, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOpt.get();


        if (!"helper".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only users with the 'requester' role can mark service requests as complete");
        }

        String coreServiceUrl = CORE_SERVICE_URL + "/"  + requestId + "?status=DONE";
        try {
            restTemplate.put(coreServiceUrl, null);
            return ResponseEntity.ok("Service request " + requestId + " marked as DONE.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Core Service: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getServiceRequestStatus(String username, String password, Long requestId) {

        Optional<User> userOpt = findByUsernameAndPassword(username, password);
        if (userOpt.isEmpty() || !userOpt.get().getRole().equals("requester")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOpt.get();

        String coreServiceUrl = CORE_SERVICE_URL +"/status/" + requestId;
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(coreServiceUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Core Service: " + e.getMessage());
        }
    }

    public ResponseEntity<?> acceptServiceRequest(String username, String password, Long requestId) {
        Optional<User> userOpt = findByUsernameAndPassword(username, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        User user = userOpt.get();

        if (!"helper".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only users with the 'helper' role can accept service requests");
        }

        String coreServiceUrl = CORE_SERVICE_URL + "/" + requestId + "?status=ACCEPTED";
        try {
            restTemplate.put(coreServiceUrl, null);
            return ResponseEntity.ok("Service request " + requestId + " has been ACCEPTED.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with Core Service: " + e.getMessage());
        }
    }


}
