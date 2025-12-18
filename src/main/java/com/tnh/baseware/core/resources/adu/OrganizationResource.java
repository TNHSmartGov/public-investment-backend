package com.tnh.baseware.core.resources.adu;

import com.tnh.baseware.core.dtos.adu.OrganizationDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.adu.Organization;
import com.tnh.baseware.core.forms.adu.OrganizationEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.IOrganizationService;

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

import java.util.List;
import java.util.UUID;

@Tag(name = "Organizations", description = "API for managing organizations")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/organizations")
public class OrganizationResource extends
                GenericResource<Organization, OrganizationEditorForm, OrganizationDTO, UUID> {

        IOrganizationService organizationService;

        public OrganizationResource(
                        IGenericService<Organization, OrganizationEditorForm, OrganizationDTO, UUID> service,
                        MessageService messageService, IOrganizationService organizationService,
                        SystemProperties systemProperties) {
                super(service, messageService, systemProperties.getApiPrefix() + "/organizations");
                this.organizationService = organizationService;
        }

        @Operation(summary = "Assign all organizations to a parent unit")
        @ApiResponse(responseCode = "200", description = "Organizations assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-organizations")
        public ResponseEntity<ApiMessageDTO<Integer>> assignOrganizations(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                organizationService.assignOrganizations(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("organizations.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove all organizations from a parent unit")
        @ApiResponse(responseCode = "200", description = "Organizations removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-organizations")
        public ResponseEntity<ApiMessageDTO<Integer>> removeOrganizations(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                organizationService.removeOrganizations(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("organizations.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Assign all users to an organization")
        @ApiResponse(responseCode = "200", description = "Users assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-users")
        public ResponseEntity<ApiMessageDTO<Integer>> assignUsers(@PathVariable UUID id,
                        @RequestBody List<UUID> userIds) {
                organizationService.assignUsers(id, userIds);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("users.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove all users from an organization")
        @ApiResponse(responseCode = "200", description = "Users removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-users")
        public ResponseEntity<ApiMessageDTO<Integer>> removeUsers(@PathVariable UUID id,
                        @RequestBody List<UUID> userIds) {
                organizationService.removeUsers(id, userIds);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("users.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }
}
