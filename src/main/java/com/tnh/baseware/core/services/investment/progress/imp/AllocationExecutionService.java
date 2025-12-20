package com.tnh.baseware.core.services.investment.progress.imp;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.mappers.investment.progress.IAllocationExecutionMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.IProjectCapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.progress.IAllocationExecutionRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.progress.IAllocationExecutionService;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AllocationExecutionService extends
        GenericService<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO, IAllocationExecutionRepository, IAllocationExecutionMapper, UUID>
        implements IAllocationExecutionService {

    IProjectCapitalAllocationRepository capitalAllocationDetailRepository;
    IProjectRepository projectRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository; // Cần thêm cái này để check hạn mức năm
    GenericEntityFetcher fetcher;

    public AllocationExecutionService(IAllocationExecutionRepository repository,
                                      IAllocationExecutionMapper mapper,
                                      IProjectCapitalAllocationRepository capitalAllocationDetailRepository,
                                      IProjectRepository projectRepository,
                                      ICapitalPlanLineRepository capitalPlanLineRepository,
                                      MessageService messageService, 
                                      GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, AllocationExecution.class);
        this.capitalAllocationDetailRepository = capitalAllocationDetailRepository;
        this.projectRepository = projectRepository;
        this.capitalPlanLineRepository = capitalPlanLineRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public AllocationExecutionDTO create(AllocationExecutionEditorForm form) {
        // 1. Kiểm tra số dư vốn năm trước khi ghi nhận thực hiện
        validateQuota(null, form);

        // 2. Map sang Entity
        var entity = mapper.formToEntity(form, fetcher, projectRepository, capitalPlanLineRepository);
        
        return mapper.entityToDTO(repository.save(entity));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public AllocationExecutionDTO update(UUID id, AllocationExecutionEditorForm form) {
        // 1. Tìm thực thể cũ
        var entity = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("allocation.execution.not.found", id)));

        // 2. Kiểm tra hạn mức (loại trừ chính nó)
        validateQuota(id, form);

        // 3. Cập nhật
        mapper.updateFromForm(form, entity, fetcher, projectRepository, capitalPlanLineRepository);
        
        return mapper.entityToDTO(repository.save(entity));
    }

    private void validateQuota(UUID currentId, AllocationExecutionEditorForm form) {
        // Lấy thông tin dòng kế hoạch năm (Cha)
        CapitalPlanLine planLine = capitalPlanLineRepository.findById(form.getCapitalPlanLineId())
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capital.planline.not.found")));

        // Tổng số tiền đã thực hiện của dòng kế hoạch này (từ DB)
        BigDecimal totalExecuted;
        if (currentId == null) {
            totalExecuted = repository.sumAmountByCapitalPlanLineId(form.getCapitalPlanLineId());
        } else {
            totalExecuted = repository.sumAmountByCapitalPlanLineIdAndExcludeId(form.getCapitalPlanLineId(), currentId);
        }

        if (totalExecuted == null) totalExecuted = BigDecimal.ZERO;

        BigDecimal limit = planLine.getAmount(); // Số vốn được giao trong năm
        BigDecimal newTotal = totalExecuted.add(form.getAmount());

        if (limit != null && limit.compareTo(newTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("allocation.execution.over.limit"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllocationExecutionDTO> getListByProjectId(UUID projectId) {
        return repository.findAllByProjectIdAndDeletedFalse(projectId).stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());
    }
}
