package com.fitness.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for Activity Service - order matters, more specific first
                .route("activity-service", r -> r
                        .path("/api/activities/**")
                        .filters(f -> f.stripPrefix(0))  // Don't strip any prefix
                        .uri("lb://ACTIVITY-SERVICE"))

                // Route for AI Service
                .route("ai-service", r -> r
                        .path("/api/recommendations/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://AI-SERVICE"))

                // Route for User Service
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("lb://USER-SERVICE"))

                .build();
    }
}
