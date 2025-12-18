package com.tnh.baseware.core.repositories.audit;

import com.tnh.baseware.core.entities.audit.EntityMetadata;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEntityMetadataRepository extends IGenericRepository<EntityMetadata, UUID> {

    /**
     * Find EntityMetadata by alias
     */
    @Query("SELECT em FROM EntityMetadata em WHERE em.alias = :alias")
    Optional<EntityMetadata> findByAlias(@Param("alias") String alias);

    /**
     * Find EntityMetadata by entity name
     */
    @Query("SELECT em FROM EntityMetadata em WHERE em.entityName = :entityName")
    Optional<EntityMetadata> findByEntityName(@Param("entityName") String entityName);

    /**
     * Find EntityMetadata by table name
     */
    @Query("SELECT em FROM EntityMetadata em WHERE em.tableName = :tableName")
    Optional<EntityMetadata> findByTableName(@Param("tableName") String tableName);

    /**
     * Check if alias exists
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.alias = :alias")
    boolean existsByAlias(@Param("alias") String alias);

    /**
     * Check if alias exists excluding current entity
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.alias = :alias AND em.id != :excludeId")
    boolean existsByAliasExcludingId(@Param("alias") String alias, @Param("excludeId") UUID excludeId);

    /**
     * Check if entity name exists
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.entityName = :entityName")
    boolean existsByEntityName(@Param("entityName") String entityName);

    /**
     * Check if entity name exists excluding current entity
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.entityName = :entityName AND em.id != :excludeId")
    boolean existsByEntityNameExcludingId(@Param("entityName") String entityName, @Param("excludeId") UUID excludeId);

    /**
     * Check if table name exists
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.tableName = :tableName")
    boolean existsByTableName(@Param("tableName") String tableName);

    /**
     * Check if table name exists excluding current entity
     */
    @Query("SELECT COUNT(em) > 0 FROM EntityMetadata em WHERE em.tableName = :tableName AND em.id != :excludeId")
    boolean existsByTableNameExcludingId(@Param("tableName") String tableName, @Param("excludeId") UUID excludeId);

    /**
     * Find all EntityMetadata ordered by alias
     */
    @Query("SELECT em FROM EntityMetadata em ORDER BY em.alias ASC")
    List<EntityMetadata> findAllOrderByAlias();

    /**
     * Find all EntityMetadata ordered by entity name
     */
    @Query("SELECT em FROM EntityMetadata em ORDER BY em.entityName ASC")
    List<EntityMetadata> findAllOrderByEntityName();

    /**
     * Search EntityMetadata by alias, entity name, or table name containing keyword
     */
    @Query("SELECT em FROM EntityMetadata em WHERE " +
            "LOWER(em.alias) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(em.entityName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(em.tableName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(em.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY em.alias ASC")
    List<EntityMetadata> searchByKeyword(@Param("keyword") String keyword);
}
