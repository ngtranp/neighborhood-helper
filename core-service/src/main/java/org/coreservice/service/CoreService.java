package org.coreservice.service;

import org.coreservice.entity.ServiceRequest;
import org.coreservice.repository.CoreRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoreService {
    @Autowired
    private CoreRepository repository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public ServiceRequest createRequest(ServiceRequest request) {
        ServiceRequest savedRequest = repository.save(request);

        String message = String.format(request.getRequesterEmail());
        amqpTemplate.convertAndSend("requestCreatedQueue", message);

        System.out.println("Sent RabbitMQ Message: " + message);

        return savedRequest;
    }

    public List<ServiceRequest> getNewRequests() {
        return repository.findByStatus("NEW");
    }

    public ServiceRequest updateStatus(Long id, String status) {
        ServiceRequest request = repository.findById(id).orElseThrow();
        request.setStatus(status);

        String message = String.format(request.getRequesterEmail());
        if(status.equals("ACCEPTED")) {
            amqpTemplate.convertAndSend("requestAcceptedQueue", message);
        }else {
            amqpTemplate.convertAndSend("requestCompletedQueue", message);
        }

        return repository.save(request);
    }

    public Optional<ServiceRequest> getRequestById(Long id) {
        return repository.findById(id);
    }


    public ServiceRequest getServiceRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service request not found for ID: " + id));
    }

}
