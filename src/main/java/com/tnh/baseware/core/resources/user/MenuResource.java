package com.tnh.baseware.core.resources.user;

import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.dtos.user.MenuDTO;
import com.tnh.baseware.core.entities.user.Menu;
import com.tnh.baseware.core.forms.user.MenuEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.user.IMenuService;
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

@Tag(name = "Menus", description = "API for managing menus")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/menus")
public class MenuResource extends GenericResource<Menu, MenuEditorForm, MenuDTO, UUID> {

        IMenuService menuService;

        public MenuResource(IGenericService<Menu, MenuEditorForm, MenuDTO, UUID> service,
                        MessageService messageService, IMenuService menuService,
                        SystemProperties systemProperties) {
                super(service, messageService, systemProperties.getApiPrefix() + "/menus");
                this.menuService = menuService;
        }

        @Operation(summary = "Assign menus to a parent unit")
        @ApiResponse(responseCode = "200", description = "Menus assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-menus")
        public ResponseEntity<ApiMessageDTO<Integer>> assignMenus(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                menuService.assignMenus(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("menus.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove menus from a parent unit")
        @ApiResponse(responseCode = "200", description = "Menus removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-menus")
        public ResponseEntity<ApiMessageDTO<Integer>> removeMenus(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                menuService.removeMenus(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("menus.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Assign roles to a menu")
        @ApiResponse(responseCode = "200", description = "Roles assigned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @PostMapping("/{id}/assign-roles")
        public ResponseEntity<ApiMessageDTO<Integer>> assignRoles(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                menuService.assignRoles(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("roles.assigned"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Remove roles from a menu")
        @ApiResponse(responseCode = "200", description = "Roles removed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @DeleteMapping("/{id}/remove-roles")
        public ResponseEntity<ApiMessageDTO<Integer>> removeRoles(@PathVariable UUID id,
                        @RequestBody List<UUID> ids) {
                menuService.removeRoles(id, ids);
                return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("roles.removed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Check if a role has access to a menu")
        @ApiResponse(responseCode = "200", description = "Access check completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/{menuId}/has-access/{roleId}")
        public ResponseEntity<ApiMessageDTO<Boolean>> hasAccess(@PathVariable UUID menuId,
                        @PathVariable UUID roleId) {
                var hasAccess = menuService.hasAccess(menuId, roleId);
                return ResponseEntity.ok(ApiMessageDTO.<Boolean>builder()
                                .data(hasAccess)
                                .result(true)
                                .message(messageService.getMessage("access.check.completed"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all menus without roles")
        @ApiResponse(responseCode = "200", description = "Menus retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/without-roles")
        public ResponseEntity<ApiMessageDTO<List<MenuDTO>>> findAllWithoutRoles() {
                var menus = menuService.findAllWithoutRoles();
                return ResponseEntity.ok(ApiMessageDTO.<List<MenuDTO>>builder()
                                .data(menus)
                                .result(true)
                                .message(messageService.getMessage("menus.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all menus without a specific role")
        @ApiResponse(responseCode = "200", description = "Menus retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/without-role/{roleId}")
        public ResponseEntity<ApiMessageDTO<List<MenuDTO>>> findAllWithoutRole(@PathVariable UUID roleId) {
                var menus = menuService.findAllWithoutRole(roleId);
                return ResponseEntity.ok(ApiMessageDTO.<List<MenuDTO>>builder()
                                .data(menus)
                                .result(true)
                                .message(messageService.getMessage("menus.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all menus by role ID")
        @ApiResponse(responseCode = "200", description = "Menus retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-role/{roleId}")
        public ResponseEntity<ApiMessageDTO<List<MenuDTO>>> findAllByRole(@PathVariable UUID roleId) {
                var menus = menuService.findAllByRole(roleId);
                return ResponseEntity.ok(ApiMessageDTO.<List<MenuDTO>>builder()
                                .data(menus)
                                .result(true)
                                .message(messageService.getMessage("menus.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }

        @Operation(summary = "Find all menus by role ID with pagination")
        @ApiResponse(responseCode = "200", description = "Menus retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
        @GetMapping("/by-role/{roleId}/pagination")
        public ResponseEntity<ApiMessageDTO<PagedModel<MenuDTO>>> findAllByRoleWithPagination(@PathVariable UUID roleId,
                        Pageable pageable,
                        PagedResourcesAssembler<MenuDTO> assembler) {
                var menus = menuService.findAllByRole(roleId, pageable);
                var pagedModel = assembler.toModel(menus, this::toModel);
                return ResponseEntity.ok(ApiMessageDTO.<PagedModel<MenuDTO>>builder()
                                .data(pagedModel)
                                .result(true)
                                .message(messageService.getMessage("menus.found"))
                                .code(HttpStatus.OK.value())
                                .build());
        }
}
