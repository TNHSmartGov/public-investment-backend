package com.tnh.baseware.core.services.audit.imp;

import com.tnh.baseware.core.annotations.ScanableEntity;
import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.dtos.audit.EntityMetadataDTO;
import com.tnh.baseware.core.entities.audit.EntityMetadata;
import com.tnh.baseware.core.enums.SpringProfile;
import com.tnh.baseware.core.forms.audit.EntityMetadataForm;
import com.tnh.baseware.core.mappers.audit.IEntityMetadataMapper;
import com.tnh.baseware.core.repositories.audit.IEntityMetadataRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.audit.IEntityMetadataService;
import com.tnh.baseware.core.utils.BasewareUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
public class EntityMetadataService extends
        GenericService<EntityMetadata, EntityMetadataForm, EntityMetadataDTO, IEntityMetadataRepository, IEntityMetadataMapper, UUID>
        implements IEntityMetadataService {
    private static final String PACKAGE_ENTITY = "com.tnh.baseware.core.entities";
    private static final String TABLE_PREFIX_PROD = "ivst_";
    private static final String TABLE_PREFIX_DEV = "";
    private static final Pattern MANY_TO_MANY_PATTERN = Pattern.compile("^[a-z_]+_[a-z_]+$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private Environment environment;

    private String tablePrefix;

    public EntityMetadataService(IEntityMetadataRepository repository,
            IEntityMetadataMapper mapper,
            MessageService messageService,
            EnumRegistry enumRegistry) {
        super(repository, mapper, messageService, EntityMetadata.class, enumRegistry);
        initializeTablePrefix();
    }

    /**
     * Khởi tạo table prefix based on active profiles
     */
    private void initializeTablePrefix() {
        boolean isProdActive = isProdProfileActive();
        tablePrefix = isProdActive ? TABLE_PREFIX_PROD : TABLE_PREFIX_DEV;
        log.info("Initialized table prefix: '{}' for {} environment", tablePrefix,
                isProdActive ? "production" : "development");
    }

    /**
     * Kiểm tra xem có đang chạy ở production profile không
     */
    private boolean isProdProfileActive() {
        if (environment == null) {
            return false;
        }
        String[] activeProfiles = environment.getActiveProfiles();
        Set<String> profileSet = new HashSet<>(Arrays.asList(activeProfiles));
        return profileSet.contains(SpringProfile.PRODUCTION.getValue());
    }

    // bỏ hàm thêm , xóa , sửa
    @Override
    public EntityMetadataDTO create(EntityMetadataForm form) {
        return null;
    }

    @Override
    public void delete(UUID id) {
        return;
    }

    @Override
    public EntityMetadataDTO update(UUID id, EntityMetadataForm form) {
        return null;
    }

    @Override
    @Transactional
    public List<EntityMetadataDTO> syncAllScanableEntities() {
        log.info("Starting synchronization of all scanable entities with table prefix: '{}'", tablePrefix);

        List<EntityMetadata> syncedEntities = syncScanableEntities(PACKAGE_ENTITY);

        // Convert to DTOs for response
        List<EntityMetadataDTO> result = syncedEntities.stream()
                .map(mapper::entityToDTO)
                .toList();

        log.info("Synchronization completed successfully. Total synced entities: {} with prefix: '{}'",
                result.size(), tablePrefix);

        // Log summary của các entities đã sync
        result.forEach(dto -> log.info("Synced entity: {} -> table: {} (alias: '{}')",
                dto.getEntityName(), dto.getTableName(), dto.getAlias()));

        return result;
    }

    @Transactional
    private List<EntityMetadata> syncScanableEntities(String basePackage) {
        List<EntityMetadata> syncedEntities = new ArrayList<>();
        List<Class<?>> scanableEntityClasses = scanForScanableEntities(basePackage);

        log.info("Found {} scanable entities to sync in package: {}", scanableEntityClasses.size(), basePackage);

        for (Class<?> entityClass : scanableEntityClasses) {
            try {
                EntityMetadata metadata = syncEntityMetadata(entityClass);
                if (metadata != null) {
                    syncedEntities.add(metadata);
                    log.debug("Successfully synced entity: {}", entityClass.getSimpleName());
                }
            } catch (Exception e) {
                log.error("Error syncing entity metadata for class: {}", entityClass.getName(), e);
            }
        }

        log.info("Entity metadata synchronization completed. Synced {} entities", syncedEntities.size());
        return syncedEntities;
    }

    /**
     * Đồng bộ một entity cụ thể
     */
    @Transactional
    private EntityMetadata syncEntityMetadata(Class<?> entityClass) {
        String entityName = entityClass.getSimpleName();
        String className = entityClass.getName();

        // Lấy table name từ @Table annotation hoặc convert từ class name
        String tableName = getTableName(entityClass);

        // Tìm existing entity metadata hoặc tạo mới
        Optional<EntityMetadata> existingMetadata = repository.findByEntityName(entityName);

        EntityMetadata metadata;
        boolean isNewEntity = false;

        if (existingMetadata.isPresent()) {
            metadata = existingMetadata.get();
            log.debug("Updating existing metadata for entity: {}", entityName);
        } else {
            metadata = EntityMetadata.builder()
                    .entityName(entityName)
                    .build();
            isNewEntity = true;
            log.debug("Creating new metadata for entity: {}", entityName);
        }

        // Cập nhật thông tin metadata
        updateEntityMetadata(metadata, entityClass, tableName, className);

        // Lưu metadata
        metadata = repository.save(metadata);

        if (isNewEntity) {
            log.info("Created new entity metadata: {} (table: {})", entityName, tableName);
        } else {
            log.info("Updated entity metadata: {} (table: {})", entityName, tableName);
        }

        return metadata;
    }

    /**
     * Quét tìm các class có @Entity và @ScanableEntity
     */
    private List<Class<?>> scanForScanableEntities(String basePackage) {
        List<Class<?>> scanableEntities = new ArrayList<>();

        try {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
                    false);

            // Scan classes có @Entity
            scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);

            for (BeanDefinition beanDefinition : candidateComponents) {
                try {
                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

                    // Chỉ lấy những entity có @ScanableEntity
                    if (clazz.isAnnotationPresent(ScanableEntity.class)) {
                        ScanableEntity annotation = clazz.getAnnotation(ScanableEntity.class);
                        scanableEntities.add(clazz);
                        log.debug("Found scanable entity: {} (name: '{}', alias: '{}') in package: {}",
                                clazz.getSimpleName(),
                                annotation.name(),
                                annotation.alias(),
                                basePackage);
                    } else {
                        log.trace("Skipping entity {} - missing @ScanableEntity annotation", clazz.getSimpleName());
                    }

                } catch (ClassNotFoundException e) {
                    log.error("Could not load class: {}", beanDefinition.getBeanClassName(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error scanning for scanable entities in package: {}", basePackage, e);
        }

        return scanableEntities;
    }

    /**
     * Lấy table name từ @Table annotation hoặc convert từ class name
     * Áp dụng naming strategy giống như CustomNamingStrategy
     */
    private String getTableName(Class<?> entityClass) {
        String originalTableName;

        // Lấy tên bảng từ @Table annotation nếu có
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            originalTableName = tableAnnotation.name();
        } else {
            // Convert CamelCase entity name to snake_case table name
            String entityName = entityClass.getSimpleName();
            originalTableName = BasewareUtils.toSnakeCase(entityName);
        }

        // Áp dụng logic từ CustomNamingStrategy
        String tableName;
        if (isManyToManyTable(originalTableName)) {
            log.debug("Detected many-to-many table: {}", originalTableName);
            tableName = originalTableName;
        } else {
            tableName = BasewareUtils.pluralize(originalTableName);
            if (!tableName.equals(originalTableName)) {
                log.debug("Pluralized table name: {} -> {}", originalTableName, tableName);
            }
        }

        // Thêm prefix
        String finalTableName = tablePrefix + tableName;
        log.debug("Final table name with prefix: {} -> {}", tableName, finalTableName);

        return finalTableName;
    }

    /**
     * Kiểm tra xem có phải many-to-many table không
     */
    private boolean isManyToManyTable(String tableName) {
        if (tableName == null || tableName.length() < 3) {
            return false;
        }
        return MANY_TO_MANY_PATTERN.matcher(tableName).matches();
    }

    /**
     * Generate alias từ entity name
     */
    private String generateAlias(String entityName) {
        // Convert EntityMetadata -> entity_metadata
        return BasewareUtils.toSnakeCase(entityName);
    }

    /**
     * Cập nhật thông tin metadata cho entity
     */
    private void updateEntityMetadata(EntityMetadata metadata, Class<?> entityClass, String tableName,
            String className) {
        // Lấy thông tin từ @ScanableEntity annotation
        ScanableEntity scanableAnnotation = entityClass.getAnnotation(ScanableEntity.class);

        // Basic information
        metadata.setTableName(tableName);

        // Sử dụng name từ @ScanableEntity nếu có, nếu không thì dùng class name
        if (scanableAnnotation != null && !scanableAnnotation.name().isEmpty()) {
            metadata.setEntityName(scanableAnnotation.name());
        } else {
            metadata.setEntityName(entityClass.getSimpleName());
        }

        // Sử dụng alias từ @ScanableEntity nếu có
        if (metadata.getAlias() == null || metadata.getAlias().isEmpty()) {
            if (scanableAnnotation != null && !scanableAnnotation.alias().isEmpty()) {
                metadata.setAlias(scanableAnnotation.alias());
            } else {
                metadata.setAlias(generateAlias(entityClass.getSimpleName()));
            }
        }

        // Sử dụng description từ @ScanableEntity nếu có
        if (metadata.getDescription() == null || metadata.getDescription().isEmpty()) {
            if (scanableAnnotation != null && !scanableAnnotation.description().isEmpty()) {
                metadata.setDescription(scanableAnnotation.description());
            } else {
                metadata.setDescription("Auto-generated metadata for " + entityClass.getSimpleName());
            }
        }

        // Analyze entity properties and store as JSON
        JSONArray propertiesJson = analyzeEntityProperties(entityClass);

        metadata.setPropertiesJson(propertiesJson.toString());

        log.debug("Updated entity metadata - Name: {}, Alias: {}, Description: {}, Table: {}",
                metadata.getEntityName(), metadata.getAlias(), metadata.getDescription(), metadata.getTableName());
    }

    /**
     * Phân tích các thuộc tính của entity và tạo JSON metadata
     */
    private JSONArray analyzeEntityProperties(Class<?> entityClass) {
        JSONArray properties = new JSONArray();

        // Lấy tất cả fields của class (bao gồm inherited fields)
        Field[] fields = getAllFields(entityClass);

        for (Field field : fields) {
            try {
                // Bỏ qua static và synthetic fields
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                JSONObject propertyInfo = new JSONObject();

                // Basic field information
                propertyInfo.put("name", field.getName());
                propertyInfo.put("type", field.getType().getSimpleName());

                properties.put(propertyInfo);

            } catch (Exception e) {
                log.warn("Error analyzing field {} in entity {}: {}", field.getName(), entityClass.getSimpleName(),
                        e.getMessage());
            }
        }

        return properties;
    }

    /**
     * Lấy tất cả fields bao gồm inherited fields
     */
    private Field[] getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // Lấy fields của class hiện tại
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            fields.add(field);
        }

        // Lấy fields của parent classes (trừ Object)
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && !superClass.equals(Object.class)) {
            Field[] superFields = superClass.getDeclaredFields();
            for (Field field : superFields) {
                fields.add(field);
            }
            superClass = superClass.getSuperclass();
        }

        return fields.toArray(new Field[0]);
    }

}
