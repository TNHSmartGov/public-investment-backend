package com.tnh.baseware.core.resources.user;

import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.dtos.user.PrivilegeDTO;
import com.tnh.baseware.core.entities.user.Privilege;
import com.tnh.baseware.core.forms.user.PrivilegeEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.user.IPrivilegesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Privileges", description = "API for managing privileges")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/privileges")
public class PrivilegeResource extends GenericResource<Privilege, PrivilegeEditorForm, PrivilegeDTO, UUID> {

        IPrivilegesService privilegeService;

        public PrivilegeResource(IGenericService<Privilege, PrivilegeEditorForm, PrivilegeDTO, UUID> service,
                        MessageService messageService, IPrivilegesService privilegeService,
                        SystemProperties systemProperties) {
                super(service, messageService, systemProperties.getApiPrefix() + "/privileges");
                this.privilegeService = privilegeService;
        }

        @Operation(summary = "Synchronize privileges")
        @ApiResponse(responseCode = "200", description = "Privileges synchronized successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/sync")
        public ResponseEntity<ApiMessageDTO<Integer>> syncPrivileges() {
                privilegeService.syncPrivileges();
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("privileges.sync.success"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all resource names")
        @ApiResponse(responseCode = "200", description = "Resource names found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/resource-names")
        public ResponseEntity<ApiMessageDTO<List<String>>> findAllResourceNames() {
                var resourceNames = privilegeService.findAllResourceNames();
                return ResponseEntity.ok(ApiMessageDTO.<List<String>>builder()
                                .data(resourceNames)
                                .result(true)
                                .message(messageService.getMessage("privileges.resource.names.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all privileges by resource name")
        @ApiResponse(responseCode = "200", description = "Privileges found by resource name", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-resource-name/{resourceName}")
        public ResponseEntity<ApiMessageDTO<List<PrivilegeDTO>>> findAllByResourceName(
                        @PathVariable String resourceName) {
                var privilegeDTOS = privilegeService.findAllByResourceName(resourceName);
                return ResponseEntity.ok(ApiMessageDTO.<List<PrivilegeDTO>>builder()
                                .data(privilegeDTOS)
                                .result(true)
                                .message(messageService.getMessage("privileges.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all privileges by resource name with pagination")
        @ApiResponse(responseCode = "200", description = "Privileges found by resource name with pagination", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-resource-name/{resourceName}/pagination")
        public ResponseEntity<ApiMessageDTO<PagedModel<PrivilegeDTO>>> findAllByResourceNameWithPagination(
                        @PathVariable String resourceName,
                        Pageable pageable,
                        PagedResourcesAssembler<PrivilegeDTO> assembler) {
                var privileges = privilegeService.findAllByResourceName(resourceName, pageable);
                var pagedModel = assembler.toModel(privileges, this::toModel);
                return ResponseEntity.ok(ApiMessageDTO.<PagedModel<PrivilegeDTO>>builder()
                                .data(pagedModel)
                                .result(true)
                                .message(messageService.getMessage("privileges.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all privileges by a role")
        @ApiResponse(responseCode = "200", description = "Privileges found by role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-role/{roleId}")
        public ResponseEntity<ApiMessageDTO<List<PrivilegeDTO>>> findAllByRole(@PathVariable UUID roleId) {
                var privilegeDTOS = privilegeService.findAllByRole(roleId);
                return ResponseEntity.ok(ApiMessageDTO.<List<PrivilegeDTO>>builder()
                                .data(privilegeDTOS)
                                .result(true)
                                .message(messageService.getMessage("privileges.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all privileges in a role with pagination")
        @ApiResponse(responseCode = "200", description = "Privileges found by role with pagination", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-role/{roleId}/pagination")
        public ResponseEntity<ApiMessageDTO<PagedModel<PrivilegeDTO>>> findAllByRoleWithPagination(
                        @PathVariable UUID roleId,
                        Pageable pageable,
                        PagedResourcesAssembler<PrivilegeDTO> assembler) {
                var privileges = privilegeService.findAllByRole(roleId, pageable);
                var pagedModel = assembler.toModel(privileges, this::toModel);
                return ResponseEntity.ok(ApiMessageDTO.<PagedModel<PrivilegeDTO>>builder()
                                .data(pagedModel)
                                .result(true)
                                .message(messageService.getMessage("privileges.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }
}
