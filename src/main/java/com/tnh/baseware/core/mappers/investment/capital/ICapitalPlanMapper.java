package com.tnh.baseware.core.mappers.investment.capital;

import org.mapstruct.*;

import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICapitalPlanMapper extends IGenericMapper<CapitalPlan, CapitalPlanEditorForm, CapitalPlanDTO> {
    
    @Named("capitalPlanToDto") // Thêm định danh này
    CapitalPlanDTO entityToDTO(CapitalPlan entity);

    CapitalPlan formToEntity(CapitalPlanEditorForm form);

    void updateCapitalFromForm(CapitalPlanEditorForm form, @MappingTarget CapitalPlan capital);
}
