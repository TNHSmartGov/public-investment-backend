package com.tnh.baseware.core.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tnh.baseware.core.exceptions.BWCGenericRuntimeException;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.utils.LogStyleHelper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class CustomRequiredFieldConverter implements ModelConverter {

    ObjectMapper objectMapper;
    MessageService messageService;

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        var schema = chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
        if (schema == null || type.getType() == null) {
            return null;
        }

        var javaType = TypeFactory.defaultInstance().constructType(type.getType());
        var clazz = javaType.getRawClass();

        try {
            var properties = objectMapper.getSerializationConfig()
                    .introspect(javaType)
                    .findProperties();

            properties.stream()
                    .filter(prop -> prop.getField() != null)
                    .forEach(prop -> {
                        var field = prop.getField();
                        if (isRequiredField(field)) {
                            schema.addRequiredItem(prop.getName());
                        }
                    });
        } catch (Exception e) {
            log.error(LogStyleHelper.error("Error while resolving schema for class: {}"), clazz.getName());
            throw new BWCGenericRuntimeException(messageService.getMessage("error.schema.resolution", clazz.getName()), e);
        }

        return schema;
    }

    private boolean isRequiredField(AnnotatedField field) {
        return field.hasAnnotation(NotNull.class) ||
                field.hasAnnotation(NotBlank.class) ||
                field.hasAnnotation(NotEmpty.class);
    }
}
