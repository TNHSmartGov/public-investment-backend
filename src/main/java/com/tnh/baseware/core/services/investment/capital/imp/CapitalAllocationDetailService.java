package com.tnh.baseware.core.services.investment.capital.imp;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDetailDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocationDetail;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.exceptions.BWCValidationException;
import com.tnh.baseware.core.forms.investment.CapitalAllocationDetailEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalAllocationDetailMapper;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationDetailRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationRepository;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.capital.ICapitalAllocationDetailService;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CapitalAllocationDetailService extends
        GenericService<CapitalAllocationDetail, CapitalAllocationDetailEditorForm, CapitalAllocationDetailDTO
        , ICapitalAllocationDetailRepository, ICapitalAllocationDetailMapper, UUID> implements ICapitalAllocationDetailService {
    
    ICapitalAllocationDetailRepository repository;
    ICapitalAllocationRepository capitalAlloCationRepository;
    ICapitalAllocationDetailMapper mapper;
    GenericEntityFetcher fetcher;

    public CapitalAllocationDetailService(ICapitalAllocationDetailRepository repository,
            ICapitalAllocationDetailMapper mapper, ICapitalAllocationRepository capitalAlloCationRepository,
            MessageService messageService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, CapitalAllocationDetail.class);
        this.repository = repository;
        this.capitalAlloCationRepository = capitalAlloCationRepository;
        this.fetcher = fetcher;
        this.mapper = mapper;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalAllocationDetailDTO create(CapitalAllocationDetailEditorForm form) {
        
        var capitalAllocationDetail = mapper.formToEntity(form, fetcher, capitalAlloCationRepository);

        BigDecimal totalCapitalAllocation = repository.findTotalAmountByCapitalAllocationId(form.getCapitalAllocationId());
        CapitalAllocation capitalAlloCation = capitalAllocationDetail.getCapitalAllocation();

        BigDecimal current = capitalAlloCation.getAmount();
        BigDecimal newTotal = totalCapitalAllocation == null ? BigDecimal.ZERO : totalCapitalAllocation.add(form.getAmount());

        if (current.compareTo(newTotal) < 0) {
            throw new BWCValidationException(messageService.getMessage("capital.allocation.over"));
        }

        return mapper.entityToDTO(repository.save(capitalAllocationDetail));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalAllocationDetailDTO update(UUID id, CapitalAllocationDetailEditorForm form) {
        var capitalAllocationDetail = repository.findById(id).orElseThrow(
                () -> new BWCNotFoundException(messageService.getMessage("capitalAllocationDetail.not.found", id)));
        mapper.updateCapitalAllocationDetailFromForm(form, capitalAllocationDetail, fetcher, capitalAlloCationRepository);
        return mapper.entityToDTO(repository.save(capitalAllocationDetail));
    }

}
