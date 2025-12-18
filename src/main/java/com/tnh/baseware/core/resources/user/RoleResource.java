package com.tnh.baseware.core.resources.user;

import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.dtos.user.RoleDTO;
import com.tnh.baseware.core.entities.user.Role;
import com.tnh.baseware.core.forms.user.RoleEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.user.IRoleService;
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

@Tag(name = "Roles", description = "API for managing roles")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/roles")
public class RoleResource extends GenericResource<Role, RoleEditorForm, RoleDTO, UUID> {

        IRoleService roleService;

        public RoleResource(IGenericService<Role, RoleEditorForm, RoleDTO, UUID> service,
                        MessageService messageService, IRoleService roleService,
                        SystemProperties systemProperties) {
                super(service, messageService, systemProperties.getApiPrefix() + "/roles");
                this.roleService = roleService;
        }

        @Operation(summary = "Assign all users to a role")
        @ApiResponse(responseCode = "200", description = "Users assigned to role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-users")
        public ResponseEntity<ApiMessageDTO<Integer>> assignUsers(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.assignUsers(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("users.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove all users from a role")
        @ApiResponse(responseCode = "200", description = "Users removed from role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-users")
        public ResponseEntity<ApiMessageDTO<Integer>> removeUsers(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.removeUsers(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("users.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Assign all privileges to a role")
        @ApiResponse(responseCode = "200", description = "Privileges assigned to role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-privileges")
        public ResponseEntity<ApiMessageDTO<Integer>> assignPrivileges(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.assignPrivileges(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("privileges.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove all privileges from a role")
        @ApiResponse(responseCode = "200", description = "Privileges removed from role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-privileges")
        public ResponseEntity<ApiMessageDTO<Integer>> removePrivileges(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.removePrivileges(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("privileges.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Assign all menus to a role")
        @ApiResponse(responseCode = "200", description = "Menus assigned to role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-menus")
        public ResponseEntity<ApiMessageDTO<Integer>> assignMenus(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.assignMenus(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("menus.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove all menus from a role")
        @ApiResponse(responseCode = "200", description = "Menus removed from role", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-menus")
        public ResponseEntity<ApiMessageDTO<Integer>> removeMenus(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                roleService.removeMenus(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("menus.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all roles by menu ID")
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-menu/{menuId}")
        public ResponseEntity<ApiMessageDTO<List<RoleDTO>>> findAllByMenu(@PathVariable UUID menuId) {
                var roles = roleService.findAllByMenu(menuId);
                return ResponseEntity.ok(ApiMessageDTO.<List<RoleDTO>>builder()
                                .data(roles)
                                .result(true)
                                .message(messageService.getMessage("roles.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all roles by privilege ID")
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-privilege/{privilegeId}")
        public ResponseEntity<ApiMessageDTO<List<RoleDTO>>> findAllByPrivilege(@PathVariable UUID privilegeId) {
                var roleDTOs = roleService.findAllByPrivilege(privilegeId);
                return ResponseEntity.ok(ApiMessageDTO.<List<RoleDTO>>builder()
                                .data(roleDTOs)
                                .result(true)
                                .message(messageService.getMessage("roles.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }
}
