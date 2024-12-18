package org.coreservice.controller;

import org.coreservice.entity.ServiceRequest;
import org.coreservice.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/core")
public class CoreController {
    @Autowired
    private CoreService service;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody ServiceRequest request) {
        return ResponseEntity.ok(service.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<?> getRequests() {
        return ResponseEntity.ok(service.getNewRequests());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getServiceRequestStatusById(@PathVariable Long id) {
        try {
            ServiceRequest request = service.getServiceRequestById(id);
            return ResponseEntity.ok(request.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service request not found for ID: " + id);
        }
    }
}
