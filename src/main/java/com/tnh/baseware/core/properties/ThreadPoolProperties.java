package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.thread.pool")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThreadPoolProperties {

    int size = 10;
    int maxSize = 20;
    int queueSize = 50;
    int keepAliveSeconds = 30;
}
