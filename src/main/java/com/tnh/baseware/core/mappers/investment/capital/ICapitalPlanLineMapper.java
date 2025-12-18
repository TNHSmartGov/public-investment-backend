package com.tnh.baseware.core.mappers.investment.capital;

import org.mapstruct.*;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.forms.investment.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
        ICapitalPlanMapper.class,
        ICapitalMapper.class
})
public interface ICapitalPlanLineMapper extends IGenericMapper<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO> {

    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    CapitalPlanLine formToEntity(CapitalPlanLineEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalPlanRepository capitalPlanRepository);
                    
    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    void updateCapitalPlanLineFromForm(CapitalPlanLineEditorForm form, @MappingTarget CapitalPlanLine capitalPlanLine,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalPlanRepository capitalPlanRepository);

}
