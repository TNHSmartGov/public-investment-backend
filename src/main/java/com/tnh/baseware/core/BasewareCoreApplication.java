package com.tnh.baseware.core;

import com.tnh.baseware.core.enums.SpringProfile;
import com.tnh.baseware.core.utils.DefaultProfileUtils;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@EnableAsync
@ConfigurationPropertiesScan
@SpringBootApplication
@EnableScheduling
public class BasewareCoreApplication {

    private final Environment env;
    private Instant startTime;

    public BasewareCoreApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        var app = new SpringApplication(BasewareCoreApplication.class);
        DefaultProfileUtils.addDefaultProfile(app);
        app.run(args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStart() {
        this.startTime = Instant.now();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationStartup() {
        if (startTime == null) {
            startTime = Instant.now();
        }

        var protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        var serverPort = Optional.ofNullable(env.getProperty("server.port")).orElse("8080");
        var contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path")).orElse("/");
        var hostAddress = Optional.ofNullable(env.getProperty("server.address")).orElse("localhost");
        var activeProfiles = DefaultProfileUtils.getActiveProfiles(env);

        var startupTime = Duration.between(startTime, Instant.now());

        log.info("""
                ----------------------------------------------------------
                \tApplication '{}' is running! Access URLs:
                \tLocal: \t{}://localhost:{}{}
                \tExternal: \t{}://{}:{}{}
                \tProfile(s): \t{}
                \tStartup time: {}ms
                \tJVM Version: {}
                ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                protocol, serverPort, contextPath,
                protocol, hostAddress, serverPort, contextPath,
                activeProfiles.isEmpty() ? "default" : activeProfiles,
                startupTime.toMillis(),
                System.getProperty("java.version"));

        validateProfiles(activeProfiles);
    }

    private void validateProfiles(List<String> activeProfiles) {
        if (activeProfiles.contains(SpringProfile.DEVELOPMENT.getValue()) &&
                activeProfiles.contains(SpringProfile.PRODUCTION.getValue())) {
            log.debug(LogStyleHelper.debug("Misconfiguration: 'dev' and 'prod' profiles should not run together."));
        } else if (activeProfiles.isEmpty()) {
            log.debug(LogStyleHelper.debug("No active profile set, using default configuration."));
        } else {
            log.info(LogStyleHelper.info("Application started with profile(s): {}"), activeProfiles);
        }
    }
}
