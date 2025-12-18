package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisProperties {

    String host = "localhost";
    int port = 6379;
    String password = "";
}
