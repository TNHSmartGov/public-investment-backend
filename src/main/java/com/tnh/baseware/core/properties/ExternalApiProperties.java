package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.external.api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalApiProperties {

    int requestTimeoutSeconds = 30;
    int maxRetryAttempts = 2;
    long retryBackoffMillis = 1000L;
    boolean circuitBreakerEnabled = true;
}
