package com.tnh.baseware.core.services.investment.progress.imp;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlanLine;
import com.tnh.baseware.core.forms.investment.progress.DisbursementEditorForm;
import com.tnh.baseware.core.mappers.investment.progress.IDisbursementMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.IProjectCapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.progress.IDisbursementRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.progress.IDisbursementService;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DisbursementService extends
        GenericService<Disbursement, DisbursementEditorForm, DisbursementDTO, IDisbursementRepository, IDisbursementMapper, UUID>
        implements IDisbursementService {

    IDisbursementRepository repository;
    IProjectRepository projectRepository;
    IProjectCapitalAllocationRepository capitalAllocationDetailRepository;
    ICapitalPlanLineRepository capitalPlanLineRepository; // Thêm để check hạn mức
    GenericEntityFetcher fetcher;

    public DisbursementService(IDisbursementRepository repository,
                               IDisbursementMapper mapper, 
                               IProjectRepository projectRepository,
                               IProjectCapitalAllocationRepository capitalAllocationDetailRepository,
                               ICapitalPlanLineRepository capitalPlanLineRepository,
                               MessageService messageService, 
                               GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, Disbursement.class);
        this.projectRepository = projectRepository;
        this.capitalAllocationDetailRepository = capitalAllocationDetailRepository;
        this.capitalPlanLineRepository = capitalPlanLineRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public DisbursementDTO create(DisbursementEditorForm form) {
        // 1. Kiểm tra hạn mức giải ngân
        validateDisbursementQuota(null, form);

        // 2. Chuyển đổi và lưu
        var disbursement = mapper.formToEntity(form, fetcher, projectRepository, capitalPlanLineRepository);
        return mapper.entityToDTO(repository.save(disbursement));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public DisbursementDTO update(UUID id, DisbursementEditorForm form) {
        // 1. Tìm bản ghi cũ
        var disbursement = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("disbursement.not.found", id)));

        // 2. Kiểm tra hạn mức (loại trừ chính nó)
        validateDisbursementQuota(id, form);

        // 3. Cập nhật dữ liệu
        mapper.updateDisbursementFromForm(form, disbursement, fetcher, projectRepository, capitalPlanLineRepository);
        
        return mapper.entityToDTO(repository.save(disbursement));
    }

    /**
     * Logic: Tổng giải ngân thực tế <= Số vốn năm được giao
     */
    private void validateDisbursementQuota(UUID currentId, DisbursementEditorForm form) {
        CapitalPlanLine planLine = capitalPlanLineRepository.findById(form.getCapitalPlanLineId())
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capital.planline.not.found")));

        // Ép kiểu để gọi hàm SUM custom
        IDisbursementRepository repo = (IDisbursementRepository) this.repository;
        
        BigDecimal totalDisbursed = (currentId == null) 
                ? repo.sumAmountByCapitalPlanLineId(form.getCapitalPlanLineId())
                : repo.sumAmountByCapitalPlanLineIdAndExcludeId(form.getCapitalPlanLineId(), currentId);
        
        if (totalDisbursed == null) totalDisbursed = BigDecimal.ZERO;

        BigDecimal limit = planLine.getAmount(); 
        BigDecimal newTotal = totalDisbursed.add(form.getAmount());

        if (limit != null && limit.compareTo(newTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("disbursement.over.limit"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisbursementDTO> getListByProjectId(UUID projectId) {
        return repository.findAllByProjectIdAndDeletedFalse(projectId).stream()
                .map(mapper::entityToDTO)
                .collect(Collectors.toList());
    }
}
