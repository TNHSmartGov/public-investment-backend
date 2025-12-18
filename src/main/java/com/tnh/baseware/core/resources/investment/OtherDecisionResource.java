package com.tnh.baseware.core.resources.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.OtherDecisionDTO;
import com.tnh.baseware.core.entities.investment.OtherDecision;
import com.tnh.baseware.core.forms.investment.OtherDecisionEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IOtherDecisionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OtherDecision", description = "API for managing OtherDecisions")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/other-decisions")
public class OtherDecisionResource extends
        GenericResource<OtherDecision, OtherDecisionEditorForm, OtherDecisionDTO, UUID> {

    public OtherDecisionResource(IGenericService<OtherDecision, OtherDecisionEditorForm, OtherDecisionDTO, UUID> service,
            MessageService messageService, IOtherDecisionService OtherDecisionService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/other-decisions");
    }

}
