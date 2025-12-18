package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.security")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityProperties {

    Jwt jwt;
    PrivilegeInfo privilege;
    Register register;
    Login login;
    String passwordDefault = "123456";

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Jwt {
        String secretKey;
        long expiration; // 1 hour
        long refreshExpiration; // 1 day
        String issuer;
        boolean allowMultipleDevices;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PrivilegeInfo {
        long ttl = 3600000; // 1 hour
        String prefix = "privileges:";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Register {
        String roleDefault = "Anonymous";
        boolean enabled = false;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Login {
        String urlLogin = "/api/v1/auth/login";
        String urlLogout = "/api/v1/auth/logout";
        String urlRefreshToken = "/api/v1/auth/refresh-token";
        int maxAttempts = 5;
        long lockTimeDuration = 300000; // 5 minutes
    }
}