package com.tnh.baseware.core.mappers.investment.capital;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.forms.investment.CapitalAllocationEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;

import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
        ICapitalPlanMapper.class,
        ICapitalMapper.class,
        ICapitalAllocationDetailMapper.class
})
public interface ICapitalAllocationMapper extends IGenericMapper<CapitalAllocation, CapitalAllocationEditorForm, CapitalAllocationDTO> {


    @Mapping(target = "capitalPlanLine", expression = "java(fetcher.formToEntity(capitalPlanLineRepository, form.getCapitalPlanLineId()))")
    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    CapitalAllocation formToEntity(CapitalAllocationEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalPlanLineRepository capitalPlanLineRepository,
                    @Context IProjectRepository projectRepository);
                    
    @Mapping(target = "capitalPlanLine", expression = "java(fetcher.formToEntity(capitalPlanLineRepository, form.getCapitalPlanLineId()))")
    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    void updateCapitalAllocationFromForm(CapitalAllocationEditorForm form, @MappingTarget CapitalAllocation captalAllocation,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalPlanLineRepository capitalPlanLineRepository,
                    @Context IProjectRepository projectRepository);

}
