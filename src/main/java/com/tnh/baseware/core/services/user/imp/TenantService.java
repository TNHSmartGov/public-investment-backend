package com.tnh.baseware.core.services.user.imp;

import com.tnh.baseware.core.entities.user.Tenant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TenantService {

    NamedParameterJdbcTemplate jdbc;
    Environment environment;

    private String getTableName() {
        String prefix = isProdProfileActive() ? "ivst_" : "";
        return "master." + prefix + "tenants";
    }

    private boolean isProdProfileActive() {
        String[] activeProfiles = environment.getActiveProfiles();
        Set<String> profileSet = new HashSet<>(Arrays.asList(activeProfiles));
        return profileSet.contains("production");
    }

    public Optional<Tenant> findByNameAndActiveTrue(String name) {
        var sql = String.format("""
                    SELECT * FROM %s
                    WHERE name = :name AND active = true
                """, getTableName());
        var result = jdbc.query(sql, Map.of("name", name), (rs, rowNum) -> map(rs));
        return result.stream().findFirst();
    }

    public List<Tenant> findAll() {

        var sql = String.format("SELECT * FROM %s ORDER BY name", getTableName());
        return jdbc.query(sql, (rs, rowNum) -> map(rs));
    }

    public void saveAllAndFlush(List<Tenant> tenants) {
        var sql = String.format("""
                INSERT INTO %s (id, name, schema_name, active, created_by, modified_by)
                VALUES (:id, :name, :schemaName, :active, :createdBy, :createdBy)
                ON CONFLICT (name) DO UPDATE
                SET id = EXCLUDED.id,
                    schema_name = EXCLUDED.schema_name,
                    active = EXCLUDED.active
                """, getTableName());
        jdbc.batchUpdate(sql, SqlParameterSourceUtils.createBatch(tenants));
    }

    private Tenant map(ResultSet rs) throws SQLException {
        return Tenant.builder()
                .id(rs.getObject("id", UUID.class))
                .name(rs.getString("name"))
                .schemaName(rs.getString("schema_name"))
                .active(rs.getBoolean("active"))
                .build();
    }
}