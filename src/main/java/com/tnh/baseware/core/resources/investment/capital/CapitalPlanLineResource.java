package com.tnh.baseware.core.resources.investment.capital;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.forms.investment.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "CapitalPlanLines", description = "API for managing CapitalPlanLines")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/capital-plan-lines")

public class CapitalPlanLineResource extends
        GenericResource<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO, UUID> {
    
    public CapitalPlanLineResource(IGenericService<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capital-plan-lines");
    }

}
