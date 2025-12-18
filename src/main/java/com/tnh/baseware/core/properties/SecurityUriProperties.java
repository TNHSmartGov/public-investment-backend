package com.tnh.baseware.core.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "baseware.core.uri")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityUriProperties {

    List<String> bypass = List.of("");
}
