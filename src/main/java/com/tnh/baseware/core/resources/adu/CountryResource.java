package com.tnh.baseware.core.resources.adu;

import com.tnh.baseware.core.dtos.adu.CountryDTO;
import com.tnh.baseware.core.entities.adu.Country;
import com.tnh.baseware.core.forms.adu.CountryEditorForm;
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

@Tag(name = "Countries", description = "Countries API")
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${baseware.core.system.api-prefix}/countries")
public class CountryResource extends
        GenericResource<Country, CountryEditorForm, CountryDTO, UUID> {

    public CountryResource(IGenericService<Country, CountryEditorForm, CountryDTO, UUID> service,
            MessageService messageService,
            SystemProperties systemProperties) {
        super(service, messageService, systemProperties.getApiPrefix() + "/countries");
    }
}
