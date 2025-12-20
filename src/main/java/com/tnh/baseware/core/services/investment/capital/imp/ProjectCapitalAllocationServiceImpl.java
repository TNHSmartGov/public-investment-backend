package com.tnh.baseware.core.services.investment.capital.imp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.ProjectCapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalPlan;
import com.tnh.baseware.core.entities.investment.capital.ProjectCapitalAllocation;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.forms.investment.capital.ProjectCapitalAllocationEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.IProjectCapitalAllocationMapper;
import com.tnh.baseware.core.repositories.investment.capital.IProjectCapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.IProjectCapitalAllocationService;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProjectCapitalAllocationServiceImpl extends
        GenericService<ProjectCapitalAllocation, ProjectCapitalAllocationEditorForm, ProjectCapitalAllocationDTO, IProjectCapitalAllocationRepository, IProjectCapitalAllocationMapper, UUID>
        implements IProjectCapitalAllocationService {

    IProjectCapitalAllocationRepository repository;
    IProjectRepository projectRepository;
    ICapitalPlanRepository capitalPlanRepository;
    GenericEntityFetcher fetcher;

    public ProjectCapitalAllocationServiceImpl(IProjectCapitalAllocationRepository repository,
                                            IProjectCapitalAllocationMapper mapper,
                                            IProjectRepository projectRepository,
                                            ICapitalPlanRepository capitalPlanRepository,
                                            MessageService messageService, 
                                            GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, ProjectCapitalAllocation.class);
        this.projectRepository = projectRepository;
        this.capitalPlanRepository = capitalPlanRepository;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectCapitalAllocationDTO create(ProjectCapitalAllocationEditorForm form) {
        // 1. Kiểm tra hạn mức nguồn vốn trung hạn trước khi giao cho dự án
        validateAllocationLimit(null, form);

        // 2. Map từ Form sang Entity (Sử dụng fetcher để load Project và CapitalPlan)
        var allocation = mapper.formToEntity(form, fetcher, projectRepository, capitalPlanRepository);

        // 3. Lưu và trả về DTO
        return mapper.entityToDTO(repository.save(allocation));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectCapitalAllocationDTO update(UUID id, ProjectCapitalAllocationEditorForm form) {
        // 1. Tìm bản ghi hiện tại
        var allocation = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.allocation.not.found", id)));

        // 2. Kiểm tra hạn mức (trừ đi số tiền cũ của chính nó)
        validateAllocationLimit(id, form);

        // 3. Cập nhật dữ liệu từ form vào entity hiện có
        mapper.updateFromForm(form, allocation, fetcher, projectRepository, capitalPlanRepository);
        
        return mapper.entityToDTO(repository.save(allocation));
    }

    /**
     * Logic kiểm tra: Tổng vốn trung hạn giao cho các dự án 
     * không được vượt quá Hạn mức tổng (totalLimit) của Nguồn vốn.
     */
    private void validateAllocationLimit(UUID currentAllocationId, ProjectCapitalAllocationEditorForm form) {
        // Lấy thông tin nguồn vốn trung hạn (Cha)
        CapitalPlan capitalPlan = capitalPlanRepository.findById(form.getCapitalPlanId())
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("capitalplan.not.found", form.getCapitalPlanId())));

        // Tính tổng số tiền đã giao cho các dự án khác từ nguồn này
        BigDecimal totalAllocated = (currentAllocationId == null) 
                ? repository.sumAmountByCapitalPlanId(form.getCapitalPlanId())
                : repository.sumAmountByCapitalPlanIdAndExcludeId(form.getCapitalPlanId(), currentAllocationId);
        
        if (totalAllocated == null) totalAllocated = BigDecimal.ZERO;

        BigDecimal limit = capitalPlan.getTotalAmountPlan(); // Hạn mức tổng của nguồn vốn
        BigDecimal newTotal = totalAllocated.add(form.getAmountInMediumTerm()); 

        if (limit != null && limit.compareTo(newTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("project.allocation.over.limit"));
        }
    }
}