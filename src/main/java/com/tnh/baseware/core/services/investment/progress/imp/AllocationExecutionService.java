package com.tnh.baseware.core.services.investment.progress.imp;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.dtos.investment.progress.AllocationExecutionDTO;
import com.tnh.baseware.core.entities.investment.progress.AllocationExecution;
import com.tnh.baseware.core.forms.investment.progress.AllocationExecutionEditorForm;
import com.tnh.baseware.core.mappers.investment.progress.IAllocationExecutionMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationDetailRepository;
import com.tnh.baseware.core.repositories.investment.progress.IAllocationExecutionRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.progress.IAllocationExecutionService;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AllocationExecutionService extends
        GenericService<AllocationExecution, AllocationExecutionEditorForm, AllocationExecutionDTO, IAllocationExecutionRepository, IAllocationExecutionMapper, UUID>
         implements
        IAllocationExecutionService {

        IAllocationExecutionRepository repository;
        IAllocationExecutionMapper mapper;
        ICapitalAllocationDetailRepository capitalAllocationDetailRepository;
        IProjectRepository projectRepository;
        GenericEntityFetcher fetcher;

        public AllocationExecutionService(IAllocationExecutionRepository repository,
            IAllocationExecutionMapper mapper,
            ICapitalAllocationDetailRepository capitalAllocationDetailRepository, IProjectRepository projectRepository,
            MessageService messageService, GenericEntityFetcher fetcher) {
                super(repository, mapper, messageService, AllocationExecution.class);
                this.repository = repository;
                this.capitalAllocationDetailRepository = capitalAllocationDetailRepository;
                this.projectRepository = projectRepository;
                this.mapper = mapper;
                this.fetcher = fetcher;
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
                public AllocationExecutionDTO create(AllocationExecutionEditorForm form) {
                var allocationExecution = mapper.formToEntity(form, fetcher, capitalAllocationDetailRepository);
                return mapper.entityToDTO(repository.save(allocationExecution));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public AllocationExecutionDTO update(UUID id, AllocationExecutionEditorForm form) {
                var execution = repository.findById(id).orElseThrow(() ->
                        new BWCNotFoundException(messageService.getMessage("allocationExecution.not.found", id)));

                mapper.updateAllocationExecutionFromForm(form, execution, fetcher, capitalAllocationDetailRepository);

                return mapper.entityToDTO(repository.save(execution));
        }

        @Override
        public List<AllocationExecutionDTO> getListByProjectId(UUID id) {
                List<AllocationExecutionDTO> allocationExecutionDTOs = mapper.entitiesToDTOs(repository.findAllByProjectId(id));
                return allocationExecutionDTOs;
        }
}
