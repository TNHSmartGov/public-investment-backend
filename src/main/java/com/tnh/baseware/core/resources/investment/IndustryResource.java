package com.tnh.baseware.core.resources.investment;

import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.entities.investment.Industry;
import com.tnh.baseware.core.forms.investment.IndustryEditorForm;
import com.tnh.baseware.core.properties.SystemProperties;
import com.tnh.baseware.core.resources.GenericResource;
import com.tnh.baseware.core.services.IGenericService;
import com.tnh.baseware.core.services.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Industries", description = "API for managing Industries")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/industries")
public class IndustryResource extends
        GenericResource<Industry, IndustryEditorForm, IndustryDTO, UUID> {

    public IndustryResource(IGenericService<Industry, IndustryEditorForm, IndustryDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/industries");
    }

}
