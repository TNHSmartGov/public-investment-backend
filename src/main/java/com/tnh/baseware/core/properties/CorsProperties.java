package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.cors")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CorsProperties {

    List<String> allowedOrigins = List.of("*");
    List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    List<String> allowedHeaders = List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With");
    List<String> exposedHeaders = List.of("Authorization", "Content-Disposition");
    boolean allowCredentials = true;
    long maxAge = 3600;
}
