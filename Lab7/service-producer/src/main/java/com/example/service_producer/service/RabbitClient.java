package com.example.service_producer.service;

import com.example.service_producer.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitClient {

    private static final Logger log = LoggerFactory.getLogger(RabbitClient.class);
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        try {
            log.info("Sending message via RabbitClient: '{}' to exchange '{}' with routing key '{}'",
                     message, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY);

            // Use the injected RabbitTemplate and constants from config
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);

            log.info("Message sent successfully via RabbitClient.");
        } catch (Exception e) {
            log.error("RabbitClient error sending message: {}", e.getMessage(), e);
            // Re-throw or handle exception as appropriate for your application
            throw new RuntimeException("Failed to send message", e);
        }
    }
} 