package com.example.notification_service.config;

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

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    @Bean
    public Exchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationOrderConfirmationQueue() {
        return new Queue("notification.order.confirmation.q");
    }

    @Bean
    public Binding bindNotificationOrderConfirmation() {
        return BindingBuilder.bind(notificationOrderConfirmationQueue())
                .to(notificationExchange())
                .with("notification.order.confirmation")
                .noargs();
    }

    @Bean
    public Queue notificationOrderStatusQueue() {
        return new Queue("notification.order.status.q");
    }

    @Bean
    public Binding bindNotificationOrderStatus() {
        return BindingBuilder.bind(notificationOrderStatusQueue())
                .to(notificationExchange())
                .with("notification.order.status")
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
