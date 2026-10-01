package com.nexus.claims.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("X-User-ID"))
                .defaultIfEmpty(exchange.getRequest().getRemoteAddress() != null 
                        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress() 
                        : "anonymous");
    }
}
