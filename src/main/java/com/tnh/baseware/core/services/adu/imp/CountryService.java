package com.tnh.baseware.core.services.adu.imp;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.dtos.adu.CountryDTO;
import com.tnh.baseware.core.entities.adu.Country;
import com.tnh.baseware.core.forms.adu.CountryEditorForm;
import com.tnh.baseware.core.mappers.adu.ICountryMapper;
import com.tnh.baseware.core.repositories.adu.ICountryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.adu.ICountryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CountryService extends
        GenericService<Country, CountryEditorForm, CountryDTO, ICountryRepository, ICountryMapper, UUID>
        implements
        ICountryService {

    public CountryService(ICountryRepository repository,
            ICountryMapper mapper,
            MessageService messageService,
            EnumRegistry enumRegistry) {
        super(repository, mapper, messageService, Country.class, enumRegistry);
    }
}
