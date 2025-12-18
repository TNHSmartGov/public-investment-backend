package com.tnh.baseware.core.services.investment.construction.imp;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.construction.LandClearance;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.services.investment.construction.ILandClearanceService;
import com.tnh.baseware.core.dtos.investment.construction.LandClearanceDTO;
import com.tnh.baseware.core.forms.investment.construction.LandClearanceEditorForm;
import com.tnh.baseware.core.mappers.investment.construction.ILandClearanceMapper;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.construction.ILandClearanceRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LandClearanceService extends
                GenericService<LandClearance, LandClearanceEditorForm, LandClearanceDTO, ILandClearanceRepository, ILandClearanceMapper, UUID>
                implements ILandClearanceService {

    ILandClearanceRepository repository;
    IProjectRepository projectRepository; 
    IFileDocumentRepository fileDocumentRepository;
    ILandClearanceMapper mapper;
    GenericEntityFetcher fetcher;
    MessageService messageService;

    public LandClearanceService(ILandClearanceRepository repository,
            ILandClearanceMapper mapper,
            IProjectRepository projectRepository,
            IFileDocumentRepository fileDocumentRepository,            
            MessageService messageService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, LandClearance.class);
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.fileDocumentRepository = fileDocumentRepository;
        this.mapper = mapper;
        this.messageService = messageService;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LandClearanceDTO create(LandClearanceEditorForm form) {
        var landClearance = mapper.formToEntity(form, fetcher, projectRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(landClearance));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public LandClearanceDTO update(UUID id, LandClearanceEditorForm form) {
        var landClearance = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("land.clearance.not.found", id)));
        mapper.updateLandClearanceFromForm(form, landClearance, fetcher, projectRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(landClearance));
    }    
}
