package com.tnh.baseware.core.services.investment.capital.imp;

import java.util.UUID;
import java.util.List;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.capital.CapitalAllocationDTO;
import com.tnh.baseware.core.entities.investment.capital.CapitalAllocation;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.CapitalAllocationEditorForm;
import com.tnh.baseware.core.mappers.investment.capital.ICapitalAllocationMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalPlanLineRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalRepository;

import org.springframework.stereotype.Service;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import com.tnh.baseware.core.services.investment.capital.ICapitalAllocationService;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CapitalAllocationService extends
        GenericService<CapitalAllocation, CapitalAllocationEditorForm, CapitalAllocationDTO, ICapitalAllocationRepository, ICapitalAllocationMapper, UUID>
        implements
        ICapitalAllocationService {

    ICapitalAllocationRepository repository;
    ICapitalAllocationMapper mapper;
    ICapitalPlanLineRepository capitalPlanLineRepository;
    IProjectRepository projectRepository;
    GenericEntityFetcher fetcher;

    public CapitalAllocationService(ICapitalAllocationRepository repository,
            ICapitalAllocationMapper mapper,
            ICapitalPlanLineRepository capitalPlanLineRepository,
            ICapitalRepository capitalRepository,
            IProjectRepository projectRepository,
            MessageService messageService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, CapitalAllocation.class);
        this.repository = repository;
        this.capitalPlanLineRepository = capitalPlanLineRepository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalAllocationDTO create(CapitalAllocationEditorForm form) {
        var capitalAllocation = mapper.formToEntity(form, fetcher, capitalPlanLineRepository, projectRepository);
        return mapper.entityToDTO(repository.save(capitalAllocation));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CapitalAllocationDTO update(UUID id, CapitalAllocationEditorForm form) {
        var capitalAllocation = repository.findById(id).orElseThrow(
                () -> new BWCNotFoundException(messageService.getMessage("capitalAllocation.not.found", id)));
        mapper.updateCapitalAllocationFromForm(form, capitalAllocation, fetcher, capitalPlanLineRepository, projectRepository);
        return mapper.entityToDTO(repository.save(capitalAllocation));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<CapitalAllocationDTO> createMany(List<CapitalAllocationEditorForm> forms) {
        var capitalAllocations = forms.stream()
            .map(form ->  mapper.formToEntity(form, fetcher, capitalPlanLineRepository, projectRepository))
            .toList();

        var savedEntities = repository.saveAll(capitalAllocations);

        return savedEntities.stream()
            .map(mapper::entityToDTO)
            .toList();
    }

    public boolean approve(UUID id) {
        CapitalAllocation capitalAllocation = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("capital.allocation.not.found", id)));
        capitalAllocation.setEligible(true);
        repository.save(capitalAllocation);
        return true;
    }

    public boolean reject(UUID id) {
        CapitalAllocation capitalAllocation = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("capital.allocation.not.found", id)));
        capitalAllocation.setEligible(false);
        repository.save(capitalAllocation);
        return true;
    }

}
