package com.tnh.baseware.core.mappers.investment.capital;

import org.mapstruct.*;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.mappers.investment.progress.IAllocationExecutionMapper;
import com.tnh.baseware.core.mappers.investment.progress.IDisbursementMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;

@Mapper(
    componentModel = "spring", 
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = { 
        ICapitalPlanMapper.class, 
        IDisbursementMapper.class, 
        IAllocationExecutionMapper.class 
    }
)
public interface ICapitalPlanLineMapper extends IGenericMapper<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO> {

    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    @Mapping(target = "disbursements", ignore = true)
    @Mapping(target = "allocationExecutions", ignore = true)
    CapitalPlanLine formToEntity(CapitalPlanLineEditorForm form,
                                @Context GenericEntityFetcher fetcher,
                                @Context ICapitalPlanRepository capitalPlanRepository);

    @Mapping(target = "capitalPlan", expression = "java(fetcher.formToEntity(capitalPlanRepository, form.getCapitalPlanId()))")
    @Mapping(target = "disbursements", ignore = true)
    @Mapping(target = "allocationExecutions", ignore = true)
    void updateFromForm(CapitalPlanLineEditorForm form, 
                        @MappingTarget CapitalPlanLine capitalPlanLine,
                        @Context GenericEntityFetcher fetcher,
                        @Context ICapitalPlanRepository capitalPlanRepository);

    // Sửa lỗi Unknown property bằng cách chỉ định đúng source và target
    @Mapping(target = "capitalPlanId", source = "capitalPlan.id")
    @Mapping(target = "capitalPlan", source = "capitalPlan", qualifiedByName = "capitalPlanToDto")
    @Mapping(target = "disbursements", source = "disbursements", qualifiedByName = "disbursementToDto")
    @Mapping(target = "allocationExecutions", source = "allocationExecutions", qualifiedByName = "executionToDto")
    // Map các phương thức tính toán từ Entity sang DTO
    @Mapping(target = "totalExecution", expression = "java(entity.getTotalAllocationExecution())")
    @Mapping(target = "totalDisbursed", expression = "java(entity.getTotalDisbursed())")
    @Mapping(target = "remainingAmount", expression = "java(entity.getRemainingAmount())")
    @Named("planLineToDTO")
    CapitalPlanLineDTO entityToDTO(CapitalPlanLine entity);
}