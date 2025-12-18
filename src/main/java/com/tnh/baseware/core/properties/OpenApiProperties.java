package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "openapi")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpenApiProperties {

    Info info;
    ContactInfo contact;
    LicenseInfo license;
    List<ServerInfo> servers;
    ExternalDocs externalDocs;
    SecurityScheme security;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {
        String title = "Baseware core API documentation";
        String description = "This is a server for Baseware core API documentation";
        String version = "1.0";
        String termsOfService = "https://starcom.io/terms/";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ContactInfo {
        String name = "Mr. Starcom";
        String email = "nvdoi.tnh@gmail.com";
        String url = "https://starcom.io";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LicenseInfo {
        String name = "Starcom License";
        String url = "https://starcom.io/license";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ServerInfo {
        String description = "Local ENV";
        String url = "https://localhost:8888";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ExternalDocs {
        String description = "Starcom website";
        String url = "https://starcom.io";
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SecurityScheme {
        String name = "bearerAuth";
        String description = "JWT Token";
        String scheme = "bearer";
        String type = "http";
        String bearerFormat = "JWT";
        String in = "header";
    }
}
