package com.tnh.baseware.core.mappers.investment.progress;

import com.tnh.baseware.core.forms.investment.progress.DisbursementEditorForm;
import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;

import org.mapstruct.*;
import com.tnh.baseware.core.components.GenericEntityFetcher;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDisbursementMapper extends IGenericMapper<Disbursement, DisbursementEditorForm, DisbursementDTO> {

    @Named("disbursementToDto") // Đánh dấu tên để Mapper cha có thể gọi chính xác
    DisbursementDTO entityToDTO(Disbursement entity);

    //@Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "capitalPlanLine", expression = "java(fetcher.formToEntity(capitalPlanLineRepository, form.getCapitalPlanLineId()))")
    @Mapping(target = "id", ignore = true)
    Disbursement formToEntity(DisbursementEditorForm form, 
                              @Context GenericEntityFetcher fetcher, 
                              @Context IProjectRepository projectRepository, 
                              @Context ICapitalPlanLineRepository capitalPlanLineRepository);

    //@InheritConfiguration(name = "formToEntityWithContext")
    void updateDisbursementFromForm(DisbursementEditorForm form, 
                                    @MappingTarget Disbursement entity, 
                                    @Context GenericEntityFetcher fetcher, 
                                    @Context IProjectRepository projectRepository, 
                                    @Context ICapitalPlanLineRepository capitalPlanLineRepository);
}

