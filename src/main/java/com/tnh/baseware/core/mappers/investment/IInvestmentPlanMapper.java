package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.dtos.investment.InvestmentPlanDTO;
import com.tnh.baseware.core.entities.investment.InvestmentPlan;
import com.tnh.baseware.core.forms.investment.InvestmentPlanEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IInvestmentPlanMapper extends IGenericMapper<InvestmentPlan, InvestmentPlanEditorForm, InvestmentPlanDTO> {

}
