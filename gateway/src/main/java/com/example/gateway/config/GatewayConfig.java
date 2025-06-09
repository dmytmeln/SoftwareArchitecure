package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final String PREFIX = "/api/v1";

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthFilter filter) {
        return builder.routes()
                .route("order-service", r -> r
                        .path("/orders/**", "/carts/**")
                        .filters(f -> f
                                .filter(filter)
                                .prefixPath(PREFIX))
                        .uri("lb://ORDER-SERVICE")
                )
                .route("inventory-service", r -> r
                        .path("/products/**", "/inventory/**")
                        .filters(f -> f
                                .filter(filter)
                                .prefixPath(PREFIX))
                        .uri("lb://INVENTORY-SERVICE")
                )
                .route("auth-service", r -> r
                        .path("/auth/**", "/users/**")
                        .filters(f -> f
                                .filter(filter)
                                .prefixPath(PREFIX))
                        .uri("lb://AUTH-SERVICE")
                )
                .route("payment-service", r -> r
                        .path("/stripe/**")
                        .filters(f -> f
                                .filter(filter)
                                .prefixPath(PREFIX))
                        .uri("lb://PAYMENT-SERVICE")
                )
                .build();
    }

}
