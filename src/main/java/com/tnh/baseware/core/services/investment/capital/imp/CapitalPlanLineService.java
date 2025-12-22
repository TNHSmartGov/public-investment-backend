package com.tnh.baseware.core.services.investment.capital.imp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalPlanLineDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.forms.investment.capital.CapitalPlanLineEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalPlanLineMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.repositories.investment.capital.IProjectCapitalAllocationRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalPlanLineService;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CapitalPlanLineService extends
        GenericService<CapitalPlanLine, CapitalPlanLineEditorForm, CapitalPlanLineDTO, ICapitalPlanLineRepository, ICapitalPlanLineMapper, UUID>
        implements ICapitalPlanLineService {

    ICapitalPlanLineRepository repository;
    ICapitalPlanRepository capitalPlanRepository;
    IProjectCapitalAllocationRepository projectCapitalAllocationRepository;
    GenericEntityFetcher fetcher;

    public CapitalPlanLineService(ICapitalPlanLineRepository repository,
                                ICapitalPlanLineMapper mapper,
                                ICapitalPlanRepository capitalPlanRepository,
                                IProjectCapitalAllocationRepository projectCapitalAllocationRepository,
                                MessageService messageService, 
                                GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, CapitalPlanLine.class);
        this.capitalPlanRepository = capitalPlanRepository;
        this.projectCapitalAllocationRepository = projectCapitalAllocationRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalPlanLineDTO create(CapitalPlanLineEditorForm form) {
        // 1. Kiểm tra hạn mức trước khi tạo
        validateQuota(null, form);

        // 2. Map và Lưu
        var capitalPlanLine = mapper.formToEntity(form, fetcher, capitalPlanRepository, projectCapitalAllocationRepository);
        return mapper.entityToDTO(repository.save(capitalPlanLine));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalPlanLineDTO update(UUID id, CapitalPlanLineEditorForm form) {
        // 1. Tìm thực thể cũ
        var capitalPlanLine = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplanline.not.found", id)));

        // 2. Kiểm tra hạn mức (loại trừ chính nó trong tổng tính toán)
        validateQuota(id, form);

        // 3. Cập nhật thông tin
        mapper.updateFromForm(form, capitalPlanLine, fetcher, capitalPlanRepository, projectCapitalAllocationRepository);
        
        return mapper.entityToDTO(repository.save(capitalPlanLine));
    }

    //Logic kiểm tra: 
    // 1. Tổng vốn các năm không được vượt quá vốn trung hạn của dự án/nguồn (QUAN TRỌNG)
    // 2. Tổng vốn các năm không được vượt quá tổng nguồn (Safe guard)
    private void validateQuota(UUID currentLineId, CapitalPlanLineEditorForm form) {
        // --- Validation 1: Kiểm tra theo Dự án (ProjectCapitalAllocation) ---
        ProjectCapitalAllocation projectAllocation = projectCapitalAllocationRepository.findById(form.getProjectCapitalAllocationId())
            .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.allocation.not.found", form.getProjectCapitalAllocationId())));

        // Kiểm tra xem Project Allocation có thuộc về Capital Plan được chọn không
        if (!projectAllocation.getCapitalPlan().getId().equals(form.getCapitalPlanId())) {
             throw new BWCValidationException(messageService.getMessage("project.allocation.capital.mismatch"));
        }

        BigDecimal projectAllocatedAmount = (currentLineId == null)
            ? repository.sumAmountByProjectAllocationId(form.getProjectCapitalAllocationId())
            : repository.sumAmountByProjectAllocationIdAndExcludeId(form.getProjectCapitalAllocationId(), currentLineId);
        
        if (projectAllocatedAmount == null) projectAllocatedAmount = BigDecimal.ZERO;
        
        BigDecimal projectLimit = projectAllocation.getAmountInMediumTerm();
        BigDecimal newProjectTotal = projectAllocatedAmount.add(form.getAmount());

        if (projectLimit != null && projectLimit.compareTo(newProjectTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("capital.planline.project.over"));
        }

        // --- Validation 2: Kiểm tra theo Tổng nguồn (CapitalPlan) ---
        CapitalPlan capitalPlan = capitalPlanRepository.findById(form.getCapitalPlanId())
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplan.not.found", form.getCapitalPlanId())));

        BigDecimal planAllocatedAmount = (currentLineId == null) 
                ? repository.sumAmountByCapitalPlanId(form.getCapitalPlanId())
                : repository.sumAmountByCapitalPlanIdAndExcludeId(form.getCapitalPlanId(), currentLineId);
        
        if (planAllocatedAmount == null) planAllocatedAmount = BigDecimal.ZERO;

        BigDecimal planLimit = capitalPlan.getTotalAmountPlan();
        BigDecimal newPlanTotal = planAllocatedAmount.add(form.getAmount());

        if (planLimit != null && planLimit.compareTo(newPlanTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("capital.planline.over"));
        }
    }
}