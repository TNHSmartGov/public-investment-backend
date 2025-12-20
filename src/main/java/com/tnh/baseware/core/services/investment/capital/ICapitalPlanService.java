package com.tnh.baseware.core.services.investment.capital;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface ICapitalPlanService extends
        IGenericService<CapitalPlan, CapitalPlanEditorForm, CapitalPlanDTO, UUID> {
}
