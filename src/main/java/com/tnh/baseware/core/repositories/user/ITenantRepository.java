package com.tnh.baseware.core.repositories.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tnh.baseware.core.entities.user.Tenant;
import com.tnh.baseware.core.repositories.IGenericRepository;

public interface ITenantRepository extends IGenericRepository<Tenant, UUID> {

    Optional<Tenant> findByNameAndActiveTrue(String name);

    Optional<Tenant> findBySchemaName(String schemaName);

    boolean existsBySchemaName(String schemaName);

    List<Tenant> findAllByActiveTrue();
}