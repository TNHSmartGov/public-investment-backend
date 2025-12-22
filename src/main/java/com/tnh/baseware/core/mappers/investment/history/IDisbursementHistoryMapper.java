package com.tnh.baseware.core.mappers.investment.history;

import com.tnh.baseware.core.dtos.investment.history.DisbursementHistoryDTO;
import com.tnh.baseware.core.entities.investment.history.DisbursementHistory;
import com.tnh.baseware.core.forms.investment.history.DisbursementHistoryEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDisbursementHistoryMapper extends IGenericMapper<DisbursementHistory, DisbursementHistoryEditorForm, DisbursementHistoryDTO> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "capitalPlanLine.id", target = "planLineId")
    DisbursementHistoryDTO entityToDTO(DisbursementHistory entity);
}
