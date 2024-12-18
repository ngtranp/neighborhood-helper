package org.notificationservice.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final AmqpTemplate rabbitTemplate;

    @Autowired
    public NotificationService(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend("notificationQueue", message);
        System.out.println("Notification sent: " + message);
    }
}
