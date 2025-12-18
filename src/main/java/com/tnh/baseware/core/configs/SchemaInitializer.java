package com.tnh.baseware.core.configs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(1) // Chạy đầu tiên
@ConditionalOnProperty(name = "baseware.core.system.initialized-enabled", havingValue = "true")
public class SchemaInitializer implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("======>>>> Initializing schemas before JPA entity creation...");

        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            // Tạo schema master trước khi Hibernate tạo bảng
            statement.execute("CREATE SCHEMA IF NOT EXISTS master");
            statement.execute("CREATE SCHEMA IF NOT EXISTS audit_public");

            log.info("Schemas initialized successfully");

        } catch (SQLException e) {
            log.error("Failed to initialize schemas", e);
            throw new RuntimeException("Schema initialization failed", e);
        }
    }

}
