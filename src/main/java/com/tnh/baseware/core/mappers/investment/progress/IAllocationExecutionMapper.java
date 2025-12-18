package com.tnh.baseware.core.mappers.investment.progress;

import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationDetailRepository;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAllocationExecutionMapper extends IGenericMapper<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO>{

    @Mapping(target = "capitalAllocationDetail", expression = "java(fetcher.formToEntity(capitalAllocationDetailRepository, form.getCapitalAllocationDetailId()))")
    AllocationExecution formToEntity(AllocationExecutionEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationDetailRepository capitalAllocationDetailRepository);

    @Mapping(target = "capitalAllocationDetail", expression = "java(fetcher.formToEntity(capitalAllocationDetailRepository, form.getCapitalAllocationDetailId()))")
    void updateAllocationExecutionFromForm (AllocationExecutionEditorForm form,
                    @MappingTarget AllocationExecution allocationExecution,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationDetailRepository capitalAllocationDetailRepository);


}
