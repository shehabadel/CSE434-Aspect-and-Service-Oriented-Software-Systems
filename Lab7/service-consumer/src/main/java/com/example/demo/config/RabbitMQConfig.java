package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Must match the producer's configuration
    public static final String EXCHANGE_NAME = "direct-exchange";
    public static final String QUEUE_NAME = "message-queue";
    public static final String ROUTING_KEY = "message.routingkey";

    @Bean
    Queue messageQueue() {
        // Declare the durable queue
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    DirectExchange directExchange() {
        // Declare the durable direct exchange
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    Binding binding(Queue messageQueue, DirectExchange directExchange) {
        // Bind the queue to the exchange with the routing key
        return BindingBuilder.bind(messageQueue)
                .to(directExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        // Ensures the queues, exchanges, and bindings are declared on the broker
        return new RabbitAdmin(connectionFactory);
    }
} 