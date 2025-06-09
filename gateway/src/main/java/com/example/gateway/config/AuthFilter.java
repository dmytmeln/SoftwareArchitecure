package com.example.gateway.config;

import com.example.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class AuthFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthFilter(RouteValidator routeValidator, JwtUtil jwtUtil) {
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        if (routeValidator.isSecured(request)) {
            return Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .filter(header -> header.startsWith("Bearer "))
                    .map(header -> header.substring(7))
                    .filter(jwtUtil::validateJwtToken)
                    .map(jwtUtil::parseClaims)
                    .map(claims -> mutateRequest(exchange, chain, claims))
                    .orElseGet(() -> onError(exchange));
        }
        return chain.filter(exchange);
    }

    private Mono<Void> mutateRequest(ServerWebExchange exchange, GatewayFilterChain chain, Claims claims) {
        var mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", jwtUtil.getIdFromClaims(claims).toString())
                .header("X-User-Role", jwtUtil.getRoleFromClaims(claims))
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

}
