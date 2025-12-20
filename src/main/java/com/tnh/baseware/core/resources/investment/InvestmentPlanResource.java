package com.tnh.baseware.core.resources.investment;

import com.tnh.baseware.core.dtos.investment.InvestmentPlanDTO;
import com.tnh.baseware.core.entities.investment.InvestmentPlan;
import com.tnh.baseware.core.forms.investment.InvestmentPlanEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IInvestmentPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Investment Plans", description = "API for managing investment plans")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/investment-plans")
public class InvestmentPlanResource extends
        GenericResource<InvestmentPlan, InvestmentPlanEditorForm, InvestmentPlanDTO, UUID> {

    public InvestmentPlanResource(IGenericService<InvestmentPlan, InvestmentPlanEditorForm, InvestmentPlanDTO, UUID> service,
                                  MessageService messageService,
                                  SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/investment-plans");
    }
}