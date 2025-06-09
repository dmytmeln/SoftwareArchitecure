package com.example.inventory_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";

    @Bean
    public Exchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Exchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue("order.created.q");
    }

    @Bean
    public Binding bindOrderCreated() {
        return BindingBuilder.bind(orderCreatedQueue())
                .to(orderExchange())
                .with("order.created")
                .noargs();
    }

    @Bean
    public Queue inventoryReservedQueue() {
        return new Queue("inventory.release.q");
    }

    @Bean
    public Binding bindInventoryReserved() {
        return BindingBuilder.bind(inventoryReservedQueue())
                .to(inventoryExchange())
                .with("inventory.release")
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRetryTemplate(getRetryTemplate());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    private RetryTemplate getRetryTemplate() {
        var retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

}
