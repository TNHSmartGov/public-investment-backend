package com.tnh.baseware.core.resources.investment.capital;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.forms.investment.CapitalPlanEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "CapitalPlans", description = "API for managing capitals")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/capitalplans")

public class CapitalPlanResource extends
        GenericResource<CapitalPlan, CapitalPlanEditorForm, CapitalPlanDTO, UUID> {
    
    public CapitalPlanResource(IGenericService<CapitalPlan, CapitalPlanEditorForm, CapitalPlanDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/capitalplans");
    }

}
