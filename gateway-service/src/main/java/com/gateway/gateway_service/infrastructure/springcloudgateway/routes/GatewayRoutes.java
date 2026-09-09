package com.gateway.gateway_service.infrastructure.springcloudgateway.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutes {

    @Bean
    public RouterFunction<ServerResponse> partServiceRoute() {
        return route("part-service")
            .route(path("/api/parts/**"), http())
            .before(stripPrefix(1))
            .filter(lb("part-service"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clientServiceRoute() {
        return route("client-service")
            .route(path("/api/clients/**"), http())
            .before(stripPrefix(1))
            .filter(lb("client-service"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> representativeServiceRoute() {
        return route("representative-service")
            .route(path("/api/representatives/**"), http())
            .before(stripPrefix(1))
            .filter(lb("representative-service"))
            .build();
    }
}
