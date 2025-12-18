package com.tnh.baseware.core.components;

import com.tnh.baseware.core.dtos.user.TenantContextDTO;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Slf4j
public final class TenantContext {

    private static final ThreadLocal<TenantContextDTO> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Private constructor to prevent instantiation
    }

    @Nullable
    public static String getTenantId() {
        return Optional.ofNullable(CURRENT_TENANT.get())
                .map(TenantContextDTO::getTenantId)
                .orElse(null);
    }

    @Nullable
    public static String getSchemaName() {
        return Optional.ofNullable(CURRENT_TENANT.get())
                .map(TenantContextDTO::getSchemaName)
                .orElse(null);
    }

    @Nullable
    public static TenantContextDTO getTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setTenant(TenantContextDTO tenant) {
        if (tenant == null) {
            log.warn(LogStyleHelper.warn("Attempted to set null tenant context"));
            return;
        }

        log.debug(LogStyleHelper.debug("Setting tenant context - ID: {}, Schema: {}"),
                tenant.getTenantId(), tenant.getSchemaName());
        CURRENT_TENANT.set(tenant);
    }

    public static void clear() {
        var currentTenant = CURRENT_TENANT.get();
        if (currentTenant != null) {
            log.debug(LogStyleHelper.debug("Clearing tenant context - ID: {}"), currentTenant.getTenantId());
        }
        CURRENT_TENANT.remove();
    }
}
