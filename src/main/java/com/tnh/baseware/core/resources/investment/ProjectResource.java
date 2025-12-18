package com.tnh.baseware.core.resources.investment;

import com.tnh.baseware.core.dtos.investment.project.ProjectCheckAprroveDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Projects", description = "API for managing projects")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/projects")
public class ProjectResource extends
        GenericResource<Project, ProjectEditorForm, ProjectDTO, UUID> {
    
    IProjectService projectService;

    public ProjectResource(IGenericService<Project, ProjectEditorForm, ProjectDTO, UUID> service,
            MessageService messageService, IProjectService projectService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/projects");
        this.projectService = projectService;
    }

    @Operation(summary = "Delete a project entity")
    @ApiResponse(responseCode = "200", description = "Delete Project Entity deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PostMapping("/delete-project/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> deleteProject(@PathVariable UUID id) {
        
        projectService.delete(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("project.deleted"))
                                .code(HttpStatus.OK.value())
                                .build());
    }

    @Operation(summary = "Approve a project entity")
    @ApiResponse(responseCode = "200", description = "Project Entity Approved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PutMapping("/approve-project/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> approveProject(@PathVariable UUID id) {
        
        projectService.approve(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("project.approved"))
                                .code(HttpStatus.OK.value())
                                .build());
    }

    @Operation(summary = "Reject a project entity")
    @ApiResponse(responseCode = "200", description = "Project Entity Rejected", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @PutMapping("/reject-project/{id}")
    public ResponseEntity<ApiMessageDTO<Integer>> rejectProject(@PathVariable UUID id, boolean is_approved) {
        
        projectService.reject(id);

        return ResponseEntity.ok(ApiMessageDTO.<Integer>builder()
                                .data(1)
                                .result(true)
                                .message(messageService.getMessage("project.rejected"))
                                .code(HttpStatus.OK.value())
                                .build());
    }

    @Operation(summary = "Get list a project to check approve")
    @ApiResponse(responseCode = "200", description = "Projects Entity Check Approve", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/check-approve-project")
    public ResponseEntity<ApiMessageDTO<List<ProjectCheckAprroveDTO>>> getListCheckApprove() {
        
        List<ProjectCheckAprroveDTO> projects = projectService.getListCheckApprove();

        return ResponseEntity.ok(ApiMessageDTO.<List<ProjectCheckAprroveDTO>>builder()
                                .data(projects)
                                .result(true)
                                .message(messageService.getMessage("projects.retrieved"))
                                .code(HttpStatus.OK.value())
                                .build());
    }


    @Operation(summary = "Get list a project by owners")
    @ApiResponse(responseCode = "200", description = "Projects Entity By Owners", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiMessageDTO.class)))
    @GetMapping("/by-owners")
    public ResponseEntity<ApiMessageDTO<List<ProjectDTO>>> getListProjectByOwners(List<UUID> ownerIds) {
        
        List<ProjectDTO> projects = projectService.getListProjectByOwners(ownerIds);

        return ResponseEntity.ok(ApiMessageDTO.<List<ProjectDTO>>builder()
                                .data(projects)
                                .result(true)
                                .message(messageService.getMessage("projects.retrieved"))
                                .code(HttpStatus.OK.value())
                                .build());
    }

}
