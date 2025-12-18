package com.tnh.baseware.core.dtos.audit;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.entities.audit.Identifiable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityMetadataDTO extends RepresentationModel<EntityMetadataDTO> implements Identifiable<UUID> {
    UUID id;
    String alias;
    String entityName;
    String tableName;
    String description;
    List<Map<String, Object>> properties;
}
