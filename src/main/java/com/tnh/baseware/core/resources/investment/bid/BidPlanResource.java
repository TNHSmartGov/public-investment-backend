package com.tnh.baseware.core.resources.investment.bid;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.bid.BidPlanDTO;
import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.forms.investment.bid.BidPlanEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.bid.IBidPlanService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "BidPlans", description = "API for managing BidPlans")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/bidplans")
public class BidPlanResource extends
        GenericResource<BidPlan, BidPlanEditorForm, BidPlanDTO, UUID> {

        public BidPlanResource(IGenericService<BidPlan,BidPlanEditorForm, BidPlanDTO, UUID> service, 
            MessageService messageService, IBidPlanService bidPlanService,
            SystemProperties systemProperties) {
            super(service, messageService, systemProperties.getApiPrefix() + "/bidplans");
        }

}
