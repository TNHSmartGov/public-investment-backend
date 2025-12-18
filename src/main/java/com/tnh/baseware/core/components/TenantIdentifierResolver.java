package com.tnh.baseware.core.components;

import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<Object> {

    private static final String DEFAULT_TENANT = "public";
    // Log every 100 occurrences or at least every 5 minutes
    private static final int LOG_FREQUENCY_THRESHOLD = 100;
    private static final long LOG_TIME_THRESHOLD_MS = 300_000; // 5 minutes
    // Log throttling parameters
    final AtomicInteger defaultSchemaCounter = new AtomicInteger(0);
    final AtomicLong lastDefaultSchemaLogTime = new AtomicLong(0);
    final AtomicLong lastTenantResolutionLogTime = new AtomicLong(0);

    @Override
    public Object resolveCurrentTenantIdentifier() {
        var schemaName = TenantContext.getSchemaName();
        if (schemaName != null) {
            if (log.isTraceEnabled()) {
                var currentTime = System.currentTimeMillis();
                var lastLogTime = lastTenantResolutionLogTime.get();

                if (currentTime - lastLogTime > LOG_TIME_THRESHOLD_MS) {
                    log.trace(LogStyleHelper.debug("Resolved tenant schema: {}"), schemaName);
                    lastTenantResolutionLogTime.set(currentTime);
                }
            }
            return schemaName;
        }

        var counter = defaultSchemaCounter.incrementAndGet();
        var currentTime = System.currentTimeMillis();
        var lastLogTime = lastDefaultSchemaLogTime.get();

        if (counter >= LOG_FREQUENCY_THRESHOLD || currentTime - lastLogTime > LOG_TIME_THRESHOLD_MS) {
            if (counter > 1) {
                log.debug(
                        LogStyleHelper
                                .debug("No tenant context found, using default schema: {} ({} times since last log)"),
                        DEFAULT_TENANT, counter);
            } else {
                log.debug(LogStyleHelper.debug("No tenant context found, using default schema: {}"), DEFAULT_TENANT);
            }

            defaultSchemaCounter.set(0);
            lastDefaultSchemaLogTime.set(currentTime);
        }

        return DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
