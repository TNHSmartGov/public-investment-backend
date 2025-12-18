package com.tnh.baseware.core.resources.investment.bid;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.dtos.investment.bid.PackageBidDTO;
import com.tnh.baseware.core.entities.investment.bid.PackageBid;
import com.tnh.baseware.core.forms.investment.bid.PackageBidEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.bid.IPackageBidService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "PackageBids", description = "API for managing BidPlans")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/package-bids")
public class PackageBidResource extends
        GenericResource<PackageBid, PackageBidEditorForm, PackageBidDTO, UUID> {

        public PackageBidResource(IGenericService<PackageBid,PackageBidEditorForm, PackageBidDTO, UUID> service, 
            MessageService messageService, IPackageBidService packageBidService,
            SystemProperties systemProperties) {
            super(service, messageService, systemProperties.getApiPrefix() + "/package-bids");
        }
        
}
