package com.example.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    private static final List<String> OPEN_ENDPOINT_PREFIXES = List.of(
            "/auth",
            "/stripe/webhook"
    );

    public boolean isSecured(ServerHttpRequest request) {
        return OPEN_ENDPOINT_PREFIXES.stream()
                .noneMatch(endpoint -> request.getURI().getPath().startsWith(endpoint));
    }

}
