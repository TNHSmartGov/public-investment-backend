package com.tnh.baseware.core.resources.investment.construction;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.entities.investment.construction.ConstructionSite;
import com.tnh.baseware.core.dtos.investment.construction.ConstructionSiteDTO;
import com.tnh.baseware.core.forms.investment.construction.ConstructionSiteEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.construction.IConstructionSiteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "ConstructionSites", description = "API for managing BidPlans")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/construction-sites")
public class ConstructionSiteResource extends
        GenericResource<ConstructionSite, ConstructionSiteEditorForm, ConstructionSiteDTO, UUID> {

        public ConstructionSiteResource(IGenericService<ConstructionSite,ConstructionSiteEditorForm, ConstructionSiteDTO, UUID> service, 
            MessageService messageService, IConstructionSiteService constructionSiteService,
            SystemProperties systemProperties) {
            super(service, messageService, systemProperties.getApiPrefix() + "/construction-sites");
        }

}
