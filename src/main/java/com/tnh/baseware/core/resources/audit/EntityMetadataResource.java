package com.tnh.baseware.core.resources.audit;

import com.tnh.baseware.core.dtos.audit.EntityMetadataDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.audit.EntityMetadata;
import com.tnh.baseware.core.forms.audit.EntityMetadataForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.audit.IEntityMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Entity Metadata", description = "API for managing entity metadata")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/entity-metadatas")
public class EntityMetadataResource extends
        GenericResource<EntityMetadata, EntityMetadataForm, EntityMetadataDTO, UUID> {

    IEntityMetadataService entityMetadataService;

    public EntityMetadataResource(IGenericService<EntityMetadata, EntityMetadataForm, EntityMetadataDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties,
            IEntityMetadataService entityMetadataService) {
        super(service, messageService, systemProperties.getApiPrefix() + "/entity-metadatas");
        this.entityMetadataService = entityMetadataService;
    }

    @Operation(summary = "Async Scannable Entities ")
    @ApiResponse(responseCode = "200", description = "Entities scanned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/async-data")
    public ResponseEntity<ApiMessageDTO<Integer>> AsyncAllData() {
        entityMetadataService.syncAllScanableEntities();
        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                .data(0)
                .result(true)
                .message(messageService.getMessage("entities.async"))
                .code(HttpStatus.OK.value())
                .build());
    }

}
