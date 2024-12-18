package org.notificationservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @RabbitListener(queues = "requestCreatedQueue")
    public void receiveRequestCreatedMessage(String email) {
        System.out.println("Request creation notification received and an email has been sent to " + email);
    }

    @RabbitListener(queues = "requestCompletedQueue")
    public void receiveRequestCompletedMessage(String email) {
        System.out.println("Service completed notification received and an email has been sent to " + email);

    }

    @RabbitListener(queues = "requestAcceptedQueue")
    public void receiveRequestAcceptedMessage(String email) {
        System.out.println("Service accepted notification received and an email has been sent to " + email);

    }
}
