package com.tnh.baseware.core.services.investment.bid.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;

import com.tnh.baseware.core.dtos.investment.bid.PackageBidDTO;
import com.tnh.baseware.core.entities.investment.bid.PackageBid;
import com.tnh.baseware.core.forms.investment.bid.PackageBidEditorForm;
import com.tnh.baseware.core.mappers.investment.bid.IPackageBidMapper;

import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.bid.IPackageBidRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.bid.IPackageBidService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PackageBidService extends
                GenericService<PackageBid,PackageBidEditorForm,PackageBidDTO,IPackageBidRepository, IPackageBidMapper, UUID> implements
                IPackageBidService {

        IPackageBidRepository repository;
        IPackageBidMapper mapper;
        IProjectRepository projectRepository;
        ICategoryRepository categoryRepository ;    
        GenericEntityFetcher fetcher;
        MessageService messageService;

        public PackageBidService(IPackageBidRepository repository,
                IPackageBidMapper mapper,
                IProjectRepository projectRepository,
                ICategoryRepository categoryRepository,  
                MessageService messageService,
                GenericEntityFetcher fetcher) {
                super(repository, mapper, messageService, PackageBid.class);
                this.repository = repository;
                this.mapper = mapper;
                this.projectRepository = projectRepository;
                this.categoryRepository = categoryRepository;
                this.messageService = messageService;
                this.fetcher = fetcher;
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public PackageBidDTO create(PackageBidEditorForm form) {
                var packageBid = mapper.formToEntity(form, fetcher, projectRepository, categoryRepository);
                return mapper.entityToDTO(repository.save(packageBid));
        }

        @Override
        @Transactional(isolation = Isolation.READ_COMMITTED)
        public PackageBidDTO update(UUID id, PackageBidEditorForm form) {
                var packageBid = repository.findById(id).orElseThrow(() ->
                        new BWCNotFoundException(messageService.getMessage("package.bid.not.found", id)));
                mapper.updatePackageBidFromForm(form, packageBid, fetcher, projectRepository, categoryRepository);
                return mapper.entityToDTO(repository.save(packageBid));
        }    
        
}

