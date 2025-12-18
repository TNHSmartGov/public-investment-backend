package com.tnh.baseware.core.services.investment.imp;

import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.entities.investment.Industry;
import com.tnh.baseware.core.forms.investment.IndustryEditorForm;
import com.tnh.baseware.core.mappers.investment.IIndustryMapper;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IIndustryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class IndustryService extends
        GenericService<Industry, IndustryEditorForm, IndustryDTO, IIndustryRepository, IIndustryMapper, UUID>
        implements
        IIndustryService {

    public IndustryService(IIndustryRepository repository,
            IIndustryMapper mapper,
            MessageService messageService) {
        super(repository, mapper, messageService, Industry.class);
    }

}
