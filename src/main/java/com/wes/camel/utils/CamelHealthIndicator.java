package com.wes.camel.utils;

import org.apache.camel.CamelContext;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor

@Slf4j
public class CamelHealthIndicator implements HealthIndicator {

    private final CamelContext camelContext;


    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {

        Health.Builder health;

        if (camelContext.isStarted()) {
            health = Health.up();
        } else {
            health = Health.down();
        }
        log.info("CamelHealthIndicator Health: {}" , health.build());
        return health.build();
    }
}