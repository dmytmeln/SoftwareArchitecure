package com.example.order_service.config;

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

    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    @Bean
    public Exchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Exchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue inventoryReservedQueue() {
        return new Queue("inventory.reserved.q");
    }

    @Bean
    public Queue inventoryFailedQueue() {
        return new Queue("inventory.failed.q");
    }

    @Bean
    public Binding bindInventoryReserved() {
        return BindingBuilder.bind(inventoryReservedQueue())
                .to(inventoryExchange())
                .with("inventory.reserved")
                .noargs();
    }

    @Bean
    public Binding bindInventoryFailed() {
        return BindingBuilder.bind(inventoryFailedQueue())
                .to(inventoryExchange())
                .with("inventory.failed")
                .noargs();
    }

    @Bean
    public Queue paymentSuccessQueue() {
        return new Queue("payment.success.q");
    }

    @Bean
    public Queue paymentFailedQueue() {
        return new Queue("payment.failed.q");
    }

    @Bean
    public Binding bindPaymentSuccess() {
        return BindingBuilder.bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with("payment.success")
                .noargs();
    }

    @Bean
    public Binding bindPaymentFailed() {
        return BindingBuilder.bind(paymentFailedQueue())
                .to(paymentExchange())
                .with("payment.failed")
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
