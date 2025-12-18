package com.tnh.baseware.core.services.investment.imp;

import java.util.UUID;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.ApprovalDecisionDTO;
import com.tnh.baseware.core.entities.investment.ApprovalDecision;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.ApprovalDecisionEditorForm;
import com.tnh.baseware.core.mappers.investment.IApprovalDecisionMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IApprovalDecisionRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.investment.IApprovalDecisionService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApprovalDecisionService extends
        GenericService<ApprovalDecision, ApprovalDecisionEditorForm, ApprovalDecisionDTO, IApprovalDecisionRepository, IApprovalDecisionMapper, UUID>
        implements
        IApprovalDecisionService {

    IApprovalDecisionRepository repository;
    IApprovalDecisionMapper mapper;
    IProjectRepository projectRepository;
    IOrganizationRepository organizationRepository;    
    IFileDocumentRepository fileDocumentRepository;
    GenericEntityFetcher fetcher;
    
    public ApprovalDecisionService(IApprovalDecisionRepository repository,
                          IApprovalDecisionMapper mapper,
                          IProjectRepository projectRepository,
                          IOrganizationRepository organizationRepository,  
                          IFileDocumentRepository fileDocumentRepository,                    
                          MessageService messageService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, ApprovalDecision.class);        
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.fileDocumentRepository = fileDocumentRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApprovalDecisionDTO create(ApprovalDecisionEditorForm form) {
        var approvalDecision = mapper.formToEntity(form, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(approvalDecision));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApprovalDecisionDTO update(UUID id, ApprovalDecisionEditorForm form) {
        var approvalDecision = repository.findById(id).orElseThrow(() ->
                new BWCNotFoundException(messageService.getMessage("approvalDecision.not.found", id)));
        mapper.updateApprovalDecisionFromForm(form, approvalDecision, fetcher, projectRepository, organizationRepository, fileDocumentRepository);
        return mapper.entityToDTO(repository.save(approvalDecision));
    }    
}
