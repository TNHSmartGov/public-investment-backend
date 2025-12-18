package com.tnh.baseware.core.mappers.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnh.baseware.core.dtos.audit.EntityMetadataDTO;
import com.tnh.baseware.core.entities.audit.EntityMetadata;
import com.tnh.baseware.core.forms.audit.EntityMetadataForm;
import com.tnh.baseware.core.mappers.IGenericMapper;

import java.util.List;
import java.util.Map;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IEntityMetadataMapper extends IGenericMapper<EntityMetadata, EntityMetadataForm, EntityMetadataDTO> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Mapping(target = "propertiesJson", source = "properties", qualifiedByName = "propertiesToString")
    EntityMetadata dtoToEntity(EntityMetadataDTO dto);

    @Override
    @Mapping(target = "properties", source = "propertiesJson", qualifiedByName = "stringToProperties")
    EntityMetadataDTO entityToDTO(EntityMetadata entity);

    @Override
    @Mapping(target = "propertiesJson", source = "properties", qualifiedByName = "propertiesToString")
    void dtoToEntity(EntityMetadataDTO dto, @MappingTarget EntityMetadata entity);

    @Override
    @Mapping(target = "properties", source = "propertiesJson", qualifiedByName = "stringToProperties")
    void entityToDTO(EntityMetadata entity, @MappingTarget EntityMetadataDTO dto);

    // Convert List<Map<String, Object>> to JSON String
    @Named("propertiesToString")
    default String propertiesToString(List<Map<String, Object>> properties) {
        if (properties == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(properties);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    // Convert JSON String to List<Map<String, Object>>
    @Named("stringToProperties")
    default List<Map<String, Object>> stringToProperties(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {
            };
            return objectMapper.readValue(jsonString, typeRef);
        } catch (JsonProcessingException e) {
            return List.of(); // Return empty list if parsing fails
        }
    }
}
