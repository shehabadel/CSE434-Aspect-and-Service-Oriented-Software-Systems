package com.example.service_producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "direct-exchange";
    public static final String QUEUE_NAME = "message-queue";
    public static final String ROUTING_KEY = "message.routingkey";

    @Bean
    Queue messageQueue() {
        // Creates a durable queue (persists broker restarts)
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    DirectExchange directExchange() {
        // Creates a durable direct exchange
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    Binding binding(Queue messageQueue, DirectExchange directExchange) {
        // Binds the queue to the exchange with the specified routing key
        return BindingBuilder.bind(messageQueue)
                .to(directExchange)
                .with(ROUTING_KEY);
    }
} 