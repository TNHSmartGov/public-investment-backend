package com.tnh.baseware.core.resources.investment;

import com.tnh.baseware.core.dtos.investment.capital.CapitalDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.forms.investment.capital.CapitalEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IProjectService;
import com.tnh.baseware.core.services.investment.capital.ICapitalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;

@Tag(name = "Projects", description = "API for managing projects")
@RestController
@RequestMapping("${baseware.core.system.api-prefix}/projects")
public class ProjectResource extends GenericResource<Project, ProjectEditorForm, ProjectDTO, UUID> {
    
    IProjectService projectService;

    public ProjectResource(IGenericService<Project, ProjectEditorForm, ProjectDTO, UUID> service,
            MessageService messageService, IProjectService projectService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capitals");
        this.projectService = projectService;
    }

    /*@PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectDTO> create(@Valid @RequestBody ProjectEditorForm form) {
        log.debug("REST request to save Project : {}", form);
        ProjectDTO result = projectService.create(form);
        return ResponseEntity
                .created(URI.create(apiPath + "/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    public ResponseEntity<ProjectDTO> update(
            @PathVariable(value = "id", required = false) final UUID id,
            @Valid @RequestBody ProjectEditorForm form) {
        log.debug("REST request to update Project : {}, {}", id, form);
        ProjectDTO result = projectService.update(id, form);
        return ResponseEntity.ok().body(result);
    }*/
}