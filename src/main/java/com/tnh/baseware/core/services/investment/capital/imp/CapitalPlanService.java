package com.tnh.baseware.core.services.investment.capital.imp;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalPlanMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalPlanService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CapitalPlanService extends
        GenericService<CapitalPlan, CapitalPlanEditorForm, CapitalPlanDTO, ICapitalPlanRepository, ICapitalPlanMapper, UUID>
        implements
        ICapitalPlanService {

    public CapitalPlanService(ICapitalPlanRepository repository,
            ICapitalPlanMapper mapper,
            MessageService messageService) {
        super(repository, mapper, messageService, CapitalPlan.class);
    }
}
