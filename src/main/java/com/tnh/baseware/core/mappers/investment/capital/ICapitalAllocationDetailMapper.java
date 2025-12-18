package com.tnh.baseware.core.mappers.investment.capital;

import java.math.BigDecimal;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDetailDTO;
import com.tnh.baseware.core.dtos.investment.capital.ReportCapitalAllocationDetailDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.forms.investment.CapitalAllocationDetailEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICapitalAllocationDetailMapper extends IGenericMapper<CapitalAllocationDetail, CapitalAllocationDetailEditorForm, CapitalAllocationDetailDTO> {

    @Mapping(target = "capitalAllocation", expression = "java(fetcher.formToEntity(capitalAllocationRepository, form.getCapitalAllocationId()))")
    CapitalAllocationDetail formToEntity(CapitalAllocationDetailEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationRepository capitalAllocationRepository);

    @Mapping(target = "capitalAllocation", expression = "java(fetcher.formToEntity(capitalAllocationRepository, form.getCapitalAllocationId()))")
    void updateCapitalAllocationDetailFromForm (CapitalAllocationDetailEditorForm form, @MappingTarget CapitalAllocationDetail capitalAllocationDetail,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationRepository capitalAllocationRepository);


    @Mapping(target = "totalDisbursement", expression = "java(calculateTotalDisbursement(detail))")
    @Mapping(target = "totalAllocationExecution", expression = "java(calculateTotalExecution(detail))")
    ReportCapitalAllocationDetailDTO toDTO(CapitalAllocationDetail detail);

    default BigDecimal calculateTotalDisbursement(CapitalAllocationDetail detail) {
        return detail.getAmount() != null ? detail.getAmount().multiply(BigDecimal.valueOf(0.8)) : null;
    }

    default BigDecimal calculateTotalExecution(CapitalAllocationDetail detail) {
        return detail.getAmount() != null ? detail.getAmount().multiply(BigDecimal.valueOf(0.9)) : null;
    }
}