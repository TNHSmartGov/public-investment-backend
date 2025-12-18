package com.tnh.baseware.core.configs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.entities.user.Tenant;
import com.tnh.baseware.core.properties.InitProperties;
import com.tnh.baseware.core.services.user.imp.TenantService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Order(2)
// @ConditionalOnProperty(name = "baseware.core.system.initialized-enabled",
// havingValue = "true")
public class TenantDataInitializer implements CommandLineRunner {
    TenantService tenantService;
    InitProperties initProperties;
    DataSource dataSource;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("======> Initializing tenant data...");

        // Khởi tạo default tenants từ config
        initDefaultTenants();

        // Tạo schemas cho các tenants
        createTenantSchemas();

        log.info("Tenant data initialization completed.");
    }

    private void initDefaultTenants() {
        log.info("Initializing default tenants in master schema...");

        List<Tenant> defaultTenants = initProperties.getTenants()
                .stream()
                .filter(config -> tenantService.findByNameAndActiveTrue(config.getName()).orElse(null) == null)
                .map(config -> {
                    Tenant tenant = Tenant.builder()
                            .id(UUID.randomUUID())
                            .name(config.getName())
                            .schemaName(config.getSchemaName())
                            .active(true)
                            .build();

                    // Manually set audit fields for initialization
                    tenant.setCreatedBy(config.getCreatedBy());
                    tenant.setModifiedBy(config.getCreatedBy());
                    tenant.setCreatedDate(LocalDateTime.now());
                    tenant.setModifiedDate(LocalDateTime.now());

                    return tenant;
                })
                .collect(Collectors.toList());

        if (!defaultTenants.isEmpty()) {
            tenantService.saveAllAndFlush(defaultTenants);
            log.info("Created {} default tenants", defaultTenants.size());
        }
    }

    private void createTenantSchemas() {
        log.info("Creating schemas for all tenants...");

        List<Tenant> tenants = tenantService.findAll();

        for (Tenant tenant : tenants) {
            createSchemaForTenant(tenant);
        }
    }

    private void createSchemaForTenant(Tenant tenant) {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {

            // Tạo tenant schema
            String tenantSchema = tenant.getSchemaName();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + tenantSchema);

        } catch (SQLException e) {
            log.error("Failed to create schemas for tenant: {}", tenant.getName(), e);
            throw new RuntimeException("Schema creation failed for tenant: " + tenant.getName(), e);
        }
    }
}
