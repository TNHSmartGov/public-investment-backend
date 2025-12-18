package com.tnh.baseware.core.services.investment.imp;

import java.util.UUID;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.OtherDecisionDTO;
import com.tnh.baseware.core.entities.investment.OtherDecision;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.OtherDecisionEditorForm;
import com.tnh.baseware.core.mappers.investment.IOtherDecisionMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IOtherDecisionRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IOtherDecisionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OtherDecisionService extends
        GenericService<OtherDecision, OtherDecisionEditorForm, OtherDecisionDTO, IOtherDecisionRepository, IOtherDecisionMapper, UUID>
        implements
        IOtherDecisionService {

    IOtherDecisionRepository repository;
    IOtherDecisionMapper mapper;
    IProjectRepository projectRepository;
    IOrganizationRepository organizationRepository;    
    IFileDocumentRepository fileDocumentRepository;
    GenericEntityFetcher fetcher;
    
    public OtherDecisionService(IOtherDecisionRepository repository,
                          IOtherDecisionMapper mapper,
                          IProjectRepository projectRepository,
                          IOrganizationRepository organizationRepository,  
                          IFileDocumentRepository fileDocumentRepository,                    
                          MessageService messageService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, OtherDecision.class);        
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.fileDocumentRepository = fileDocumentRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OtherDecisionDTO create(OtherDecisionEditorForm form) {
        var approvalDecision = mapper.formToEntity(form, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(approvalDecision));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OtherDecisionDTO update(UUID id, OtherDecisionEditorForm form) {
        var otherDecision = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("other.decision.not.found", id)));
        mapper.updateApprovalDecisionFromForm(form, otherDecision, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(otherDecision));
    }    
}
