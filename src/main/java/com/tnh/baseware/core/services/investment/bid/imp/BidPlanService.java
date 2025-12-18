package com.tnh.baseware.core.services.investment.bid.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.EnumRegistry;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.bid.BidPlanDTO;
import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.bid.BidPlanEditorForm;
import com.tnh.baseware.core.mappers.investment.bid.IBidPlanMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.bid.IBidPlanRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.bid.IBidPlanService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BidPlanService extends
                GenericService<BidPlan,BidPlanEditorForm,BidPlanDTO,IBidPlanRepository, IBidPlanMapper, UUID> implements
                IBidPlanService {

        IBidPlanRepository repository;
        IBidPlanMapper mapper;
        IProjectRepository projectRepository;
        IOrganizationRepository organizationRepository;    
        IFileDocumentRepository fileDocumentRepository;
        GenericEntityFetcher fetcher;
        MessageService messageService;

        public BidPlanService(IBidPlanRepository repository,
                IBidPlanMapper mapper,
                IProjectRepository projectRepository,
                IOrganizationRepository organizationRepository,  
                IFileDocumentRepository fileDocumentRepository,
                MessageService messageService,
                GenericEntityFetcher fetcher,
                EnumRegistry enumRegistry) {
                super(repository, mapper, messageService, BidPlan.class, enumRegistry);
                this.repository = repository;
                this.mapper = mapper;
                this.projectRepository = projectRepository;
                this.organizationRepository = organizationRepository;
                this.fileDocumentRepository = fileDocumentRepository;
                this.messageService = messageService;
                this.fetcher = fetcher;
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public BidPlanDTO create(BidPlanEditorForm form) {
                var bidPlan = mapper.formToEntity(form, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
                return mapper.entityToDTO(repository.save(bidPlan));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public BidPlanDTO update(UUID id, BidPlanEditorForm form) {
                var bidPlan = repository.findById(id).orElseThrow(() ->
                        new BWCNotFoundException(messageService.getMessage("bidPlan.not.found", id)));
                mapper.updateBidPlanFromForm(form, bidPlan, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
                return mapper.entityToDTO(repository.save(bidPlan));
        }    
        
}
