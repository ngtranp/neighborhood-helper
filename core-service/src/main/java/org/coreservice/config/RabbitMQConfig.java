package org.coreservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue RequestCreatedQueue() {
        return new Queue("requestCreatedQueue");
    }

    @Bean
    public Queue RequestCompletedQueue() {
        return new Queue("requestCompletedQueue");
    }

    @Bean
    public Queue RequestAcceptedQueue() {
        return new Queue("requestAcceptedQueue");
    }
}
