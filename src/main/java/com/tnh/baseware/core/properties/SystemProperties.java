package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.system")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SystemProperties {

    String apiPrefix = "/api/v1";
    String name = "Baseware Core";
    String version = "1.0.0";
    String description = "Baseware Core is a core module of Baseware Platform";
    boolean trackingActive = false;
    boolean initializedEnabled = false;
    long cachePatternClearIntervalMs = 7200000; // 2 hours
    String tokenClearInterval = "0 0 0 * * *"; // Every day at 00:00:00
}
