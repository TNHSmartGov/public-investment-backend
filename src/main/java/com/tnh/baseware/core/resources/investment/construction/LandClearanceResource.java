package com.tnh.baseware.core.resources.investment.construction;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnh.baseware.core.entities.investment.construction.LandClearance;
import com.tnh.baseware.core.dtos.investment.construction.LandClearanceDTO;
import com.tnh.baseware.core.forms.investment.construction.LandClearanceEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.construction.ILandClearanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Tag(name = "LandClearances", description = "API for managing LandClearances")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/land-clearances")
public class LandClearanceResource extends
        GenericResource<LandClearance, LandClearanceEditorForm, LandClearanceDTO, UUID> {

        public LandClearanceResource(IGenericService<LandClearance,LandClearanceEditorForm, LandClearanceDTO, UUID> service, 
            MessageService messageService, ILandClearanceService landClearanceService,
            SystemProperties systemProperties) {
            super(service, messageService, systemProperties.getApiPrefix() + "/land-clearances");
        }

}
