package com.example.demo.listener;

import com.example.demo.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        log.info("Received message from queue '{}': '{}'", RabbitMQConfig.QUEUE_NAME, message);

        // Add your message processing logic here
        try {
            // Simulate processing
            Thread.sleep(100); // Simulate work
            log.info("Successfully processed message: '{}'", message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Processing interrupted for message: '{}'", message, e);
            // Consider re-throwing or handling appropriately if processing must complete
        } catch (Exception e) {
            log.error("Failed to process message: '{}'", message, e);
            // By default, if an exception occurs, the message might be requeued
            // depending on configuration (see application.properties retry/requeue settings).
            // Throwing AmqpRejectAndDontRequeueException ensures it's not requeued.
        }
    }
} 