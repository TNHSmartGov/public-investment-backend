package com.tnh.baseware.core.resources.investment.capital;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.capital.ProjectCapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.forms.investment.capital.ProjectCapitalAllocationEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "ProjectCapitalAllocations", description = "API for managing project capital allocations")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/project-capital-allocations")

public class ProjectCapitalAllocationResource extends
        GenericResource<ProjectCapitalAllocation, ProjectCapitalAllocationEditorForm, ProjectCapitalAllocationDTO, UUID> {
    
    public ProjectCapitalAllocationResource(IGenericService<ProjectCapitalAllocation, ProjectCapitalAllocationEditorForm, ProjectCapitalAllocationDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/project-capital-allocations");
    }

}
