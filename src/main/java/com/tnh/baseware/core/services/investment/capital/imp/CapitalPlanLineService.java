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
    GenericEntityFetcher fetcher;

    public CapitalPlanLineService(ICapitalPlanLineRepository repository,
                                ICapitalPlanLineMapper mapper,
                                ICapitalPlanRepository capitalPlanRepository,
                                MessageService messageService, 
                                GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, CapitalPlanLine.class);
        this.capitalPlanRepository = capitalPlanRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalPlanLineDTO create(CapitalPlanLineEditorForm form) {
        // 1. Kiểm tra hạn mức trước khi tạo
        validateQuota(null, form);

        // 2. Map và Lưu
        var capitalPlanLine = mapper.formToEntity(form, fetcher, capitalPlanRepository);
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
        mapper.updateFromForm(form, capitalPlanLine, fetcher, capitalPlanRepository);
        
        return mapper.entityToDTO(repository.save(capitalPlanLine));
    }

    //Logic kiểm tra: Tổng vốn các năm không được vượt quá vốn trung hạn của dự án/nguồn
    private void validateQuota(UUID currentLineId, CapitalPlanLineEditorForm form) {
        // Lấy thông tin kế hoạch vốn tổng (Cha)
        CapitalPlan capitalPlan = capitalPlanRepository.findById(form.getCapitalPlanId())
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplan.not.found", form.getCapitalPlanId())));

        // Tính tổng đã phân bổ trong database (trừ dòng hiện tại nếu là update)
        BigDecimal allocatedAmount = (currentLineId == null) 
                ? repository.sumAmountByCapitalPlanId(form.getCapitalPlanId())
                : repository.sumAmountByCapitalPlanIdAndExcludeId(form.getCapitalPlanId(), currentLineId);
        
        if (allocatedAmount == null) allocatedAmount = BigDecimal.ZERO;

        BigDecimal limit = capitalPlan.getTotalAmountPlan(); // Hạn mức tổng
        BigDecimal newTotal = allocatedAmount.add(form.getAmount()); // Tổng mới nếu lưu thành công

        if (limit != null && limit.compareTo(newTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("capital.planline.over"));
        }
    }
}