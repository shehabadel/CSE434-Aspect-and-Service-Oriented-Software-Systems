package com.example.service_producer.controller;

import com.example.service_producer.service.RabbitClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final RabbitClient rabbitClient;

    @Autowired
    public MessageController(RabbitClient rabbitClient) {
        this.rabbitClient = rabbitClient;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        try {
            log.info("Controller received request to send message: '{}'", message);
            rabbitClient.sendMessage(message);
            return ResponseEntity.ok("Message sent successfully via controller: " + message);
        } catch (Exception e) {
            log.error("Controller error processing send message request: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error sending message: " + e.getMessage());
        }
    }
} 