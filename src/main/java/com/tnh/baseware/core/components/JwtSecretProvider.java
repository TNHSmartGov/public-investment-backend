package com.tnh.baseware.core.components;

import com.tnh.baseware.core.properties.SecurityProperties;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtSecretProvider {

    final SecurityProperties securityProperties;
    byte[] decodedSecret;

    public JwtSecretProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void init() {
        this.decodedSecret = Base64.getDecoder().decode(securityProperties.getJwt().getSecretKey());
    }

    public byte[] getDecodedSecretKey() {
        return decodedSecret;
    }
}