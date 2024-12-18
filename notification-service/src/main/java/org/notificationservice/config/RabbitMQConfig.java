package org.notificationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue requestCreatedQueue() {
        return new Queue("requestCreatedQueue", true);
    }

    @Bean
    public Queue requestCompletedQueue() {
        return new Queue("requestCompletedQueue", true);
    }

    @Bean
    public Queue requestAcceptedQueue() {
        return new Queue("requestAcceptedQueue", true);
    }
}
