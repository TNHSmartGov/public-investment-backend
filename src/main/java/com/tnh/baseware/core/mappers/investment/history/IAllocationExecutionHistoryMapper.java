package com.tnh.baseware.core.mappers.investment.history;

import com.tnh.baseware.core.dtos.investment.history.AllocationExecutionHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.AllocationExecutionHistory;
import com.tnh.baseware.core.forms.investment.history.AllocationExecutionHistoryEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAllocationExecutionHistoryMapper extends IGenericMapper<AllocationExecutionHistory, AllocationExecutionHistoryEditorForm, AllocationExecutionHistoryDTO> {
    
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "capitalPlanLine.id", target = "planLineId")
    AllocationExecutionHistoryDTO entityToDTO(AllocationExecutionHistory entity);
}
