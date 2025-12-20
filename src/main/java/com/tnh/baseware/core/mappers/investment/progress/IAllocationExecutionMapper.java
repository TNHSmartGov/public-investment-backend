package com.tnh.baseware.core.mappers.investment.progress;

import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAllocationExecutionMapper extends IGenericMapper<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO> {

    @Named("executionToDto") // Định danh để ICapitalPlanLineMapper gọi tới
    AllocationExecutionDTO entityToDTO(AllocationExecution entity);

    //@Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "capitalPlanLine", expression = "java(fetcher.formToEntity(capitalPlanLineRepository, form.getCapitalPlanLineId()))")
    AllocationExecution formToEntity(AllocationExecutionEditorForm form,
                                     @Context GenericEntityFetcher fetcher,
                                     @Context IProjectRepository projectRepository, // Phải có tham số này
                                     @Context ICapitalPlanLineRepository capitalPlanLineRepository);

    //@InheritConfiguration(name = "formToEntityWithContext")
    void updateFromForm(AllocationExecutionEditorForm form, 
                        @MappingTarget AllocationExecution entity,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context ICapitalPlanLineRepository capitalPlanLineRepository);
}
