package com.tnh.baseware.core.mappers.investment.progress;

import com.tnh.baseware.core.forms.investment.progress.DisbursementEditorForm;
import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationDetailRepository;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDisbursementMapper extends IGenericMapper<Disbursement, DisbursementEditorForm, DisbursementDTO>{

    @Mapping(target = "capitalAllocationDetail", expression = "java(fetcher.formToEntity(capitalAllocationDetailRepository, form.getCapitalAllocationDetailId()))")
    Disbursement formToEntity(DisbursementEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationDetailRepository capitalAllocationDetailRepository);

    @Mapping(target = "capitalAllocationDetail", expression = "java(fetcher.formToEntity(capitalAllocationDetailRepository, form.getCapitalAllocationDetailId()))")
    void updateDisbursementFromForm (DisbursementEditorForm form,
                    @MappingTarget Disbursement disbursement,
                    @Context GenericEntityFetcher fetcher,
                    @Context ICapitalAllocationDetailRepository capitalAllocationDetailRepository);

}

