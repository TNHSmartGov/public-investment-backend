package com.tnh.baseware.core.services.investment.capital;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ICapitalPlanLineService extends
        IGenericService<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO, UUID> {

        @Override
        CapitalPlanLineDTO create(CapitalPlanLineEditorForm form);

        @Override
        CapitalPlanLineDTO update(UUID id, CapitalPlanLineEditorForm form);
}
