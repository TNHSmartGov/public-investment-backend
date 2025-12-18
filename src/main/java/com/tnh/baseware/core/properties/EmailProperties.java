package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.email")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailProperties {

    String host = "smtp.gmail.com";
    int port = 587;
    String username = "";
    String password = "";
    String transportProtocol = "smtp";
    boolean smtpAuth = true;
    boolean smtpStarttlsEnable = true;
    boolean debug = false;
}
