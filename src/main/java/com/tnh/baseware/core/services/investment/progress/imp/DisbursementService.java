package com.tnh.baseware.core.services.investment.progress.imp;

import org.springframework.stereotype.Service;

import com.tnh.baseware.core.dtos.investment.progress.DisbursementDTO;
import com.tnh.baseware.core.entities.investment.progress.Disbursement;
import com.tnh.baseware.core.forms.investment.progress.DisbursementEditorForm;
import com.tnh.baseware.core.mappers.investment.progress.IDisbursementMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationDetailRepository;
import com.tnh.baseware.core.repositories.investment.progress.IDisbursementRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.progress.IDisbursementService;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DisbursementService extends
        GenericService<Disbursement, DisbursementEditorForm, DisbursementDTO, IDisbursementRepository, IDisbursementMapper, UUID>
         implements
        IDisbursementService {

        IDisbursementRepository repository;
        IDisbursementMapper mapper;
        IProjectRepository projectRepository;
        ICapitalAllocationDetailRepository capitalAllocationDetailRepository;
        GenericEntityFetcher fetcher;

        public DisbursementService(IDisbursementRepository repository,
            IDisbursementMapper mapper, IProjectRepository projectRepository,
            ICapitalAllocationDetailRepository capitalAllocationDetailRepository,
            MessageService messageService, GenericEntityFetcher fetcher) {
                super(repository, mapper, messageService, Disbursement.class);
                this.repository = repository;
                this.capitalAllocationDetailRepository = capitalAllocationDetailRepository;
                this.projectRepository = projectRepository;
                this.mapper = mapper;
                this.fetcher = fetcher;
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
                public DisbursementDTO create(DisbursementEditorForm form) {
                var disbursement = mapper.formToEntity(form, fetcher, capitalAllocationDetailRepository);
                return mapper.entityToDTO(repository.save(disbursement));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public DisbursementDTO update(UUID id, DisbursementEditorForm form) {
                var disbursement = repository.findById(id).orElseThrow(() ->
                        new BWCNotFoundException(messageService.getMessage("disbursement.not.found", id)));

                mapper.updateDisbursementFromForm(form, disbursement, fetcher, capitalAllocationDetailRepository);

                return mapper.entityToDTO(repository.save(disbursement));
        }

        @Override
        public List<DisbursementDTO> getListByProjectId(UUID id) {
                List<DisbursementDTO> disbursementDTOs = mapper.entitiesToDTOs(repository.findAllByProjectId(id));
                return disbursementDTOs;
        }

}
