package com.tnh.baseware.core.services.investment;

import java.util.UUID;

import com.tnh.baseware.core.dtos.investment.InvestmentPlanDTO;
import com.tnh.baseware.core.entities.investment.InvestmentPlan;
import com.tnh.baseware.core.forms.investment.InvestmentPlanEditorForm;
import com.tnh.baseware.core.services.IGenericService;

public interface IInvestmentPlanService extends
        IGenericService<InvestmentPlan, InvestmentPlanEditorForm, InvestmentPlanDTO, UUID> {

}
