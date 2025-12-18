package com.tnh.baseware.core.components;

import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import com.tnh.baseware.core.exceptions.BWCSQLCustomException;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TenantConnectionProvider implements MultiTenantConnectionProvider<Object> {

    DataSource dataSource;
    MessageService messageService;

    @Override
    public Connection getAnyConnection() {
        try {
            log.debug(LogStyleHelper.debug("Getting any connection from data source"));
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error(LogStyleHelper.error("Failed to get a non-tenant (generic) connection: {}"), e.getMessage());
            throw new BWCSQLCustomException(messageService.getMessage("error.db.connection.generic"), e);
        }
    }

    @Override
    public void releaseAnyConnection(Connection connection) {
        if (connection == null) {
            log.warn(LogStyleHelper.warn("Attempted to release null connection"));
            return;
        }

        try {
            connection.close();
            log.debug(LogStyleHelper.debug("Released a non-tenant (generic) connection"));
        } catch (SQLException e) {
            log.error(LogStyleHelper.error("Failed to release non-tenant connection: {}"), e.getMessage());
            throw new BWCSQLCustomException(messageService.getMessage("error.db.release.generic"), e);
        }
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) {
        try {
            var schemaName = (String) tenantIdentifier;
            log.debug(LogStyleHelper.debug("Getting connection for tenant schema: {}"), schemaName);

            var connection = getAnyConnection();
            connection.setSchema(schemaName);

            return connection;
        } catch (SQLException e) {
            log.error(LogStyleHelper.error("Failed to get a connection for tenant: {}"), tenantIdentifier);
            throw new BWCSQLCustomException(messageService.getMessage("error.db.connection.tenant", tenantIdentifier),
                    e);
        } catch (ClassCastException e) {
            log.error(LogStyleHelper.error("Invalid tenant identifier type: {}"),
                    tenantIdentifier != null ? tenantIdentifier.getClass().getName() : "null");
            throw new BWCGenericRuntimeException(messageService.getMessage("error.tenant.invalid.type"), e);
        }
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) {
        if (connection == null) {
            log.warn(LogStyleHelper.warn("Attempted to release null connection for tenant: {}"), tenantIdentifier);
            return;
        }

        try {
            var schemaName = (String) tenantIdentifier;
            log.debug(LogStyleHelper.debug("Releasing connection for tenant schema: {}"), schemaName);

            connection.setSchema("public");
            releaseAnyConnection(connection);
        } catch (SQLException e) {
            log.error(LogStyleHelper.error("Failed to release tenant connection: {}"), e.getMessage());
            throw new BWCSQLCustomException(messageService.getMessage("error.db.release.tenant", tenantIdentifier), e);
        } catch (ClassCastException e) {
            log.error(LogStyleHelper.error("Invalid tenant identifier type during release: {}"),
                    tenantIdentifier != null ? tenantIdentifier.getClass().getName() : "null");
            throw new BWCGenericRuntimeException(messageService.getMessage("error.tenant.invalid.type"), e);
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(@NonNull Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@NonNull Class<T> unwrapType) {
        log.debug(LogStyleHelper.debug("Unwrap request for unsupported type: {}"), unwrapType.getSimpleName());
        throw new BWCGenericRuntimeException(messageService.getMessage("error.unwrap.unsupported"));
    }
}