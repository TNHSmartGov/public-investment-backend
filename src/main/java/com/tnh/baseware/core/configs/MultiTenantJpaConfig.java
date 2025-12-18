package com.tnh.baseware.core.configs;

import com.tnh.baseware.core.components.TenantConnectionProvider;
import com.tnh.baseware.core.components.TenantIdentifierResolver;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MultiTenantJpaConfig {

    DataSource dataSource;
    TenantConnectionProvider tenantConnectionProvider;
    TenantIdentifierResolver tenantIdentifierResolver;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        log.debug(LogStyleHelper.debug("Configuring multi-tenant EntityManagerFactory"));

        var properties = new HashMap<String, Object>();
        properties.put("hibernate.multiTenancy", "SCHEMA");
        properties.put("hibernate.multi_tenant_connection_provider", tenantConnectionProvider);
        properties.put("hibernate.tenant_identifier_resolver", tenantIdentifierResolver);
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", true);

        var entityManagerFactory = builder
                .dataSource(dataSource)
                .packages("com.tnh.baseware.core.entities")
                .persistenceUnit("default")
                .properties(properties)
                .build();

        log.trace(LogStyleHelper.trace("Multi-tenant EntityManagerFactory configured successfully"));
        return entityManagerFactory;
    }
}
