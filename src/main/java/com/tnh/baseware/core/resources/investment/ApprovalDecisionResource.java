package com.tnh.baseware.core.resources.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.ApprovalDecisionDTO;
import com.tnh.baseware.core.entities.investment.ApprovalDecision;
import com.tnh.baseware.core.forms.investment.ApprovalDecisionEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IApprovalDecisionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ApprovalDecision", description = "API for managing ApprovalDecisions")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/approval-decisions")
public class ApprovalDecisionResource extends
        GenericResource<ApprovalDecision, ApprovalDecisionEditorForm, ApprovalDecisionDTO, UUID> {

    public ApprovalDecisionResource(IGenericService<ApprovalDecision, ApprovalDecisionEditorForm, ApprovalDecisionDTO, UUID> service,
            MessageService messageService, IApprovalDecisionService approvalDecisionService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/approval-decisions");
    }

}
