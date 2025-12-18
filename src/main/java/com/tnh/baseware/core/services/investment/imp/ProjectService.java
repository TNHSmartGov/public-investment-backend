package com.tnh.baseware.core.services.investment.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.project.ProjectCheckAprroveDTO;
import com.tnh.baseware.core.dtos.investment.project.ProjectDTO;
import com.tnh.baseware.core.entities.investment.Project;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.forms.investment.ProjectEditorForm;
import com.tnh.baseware.core.mappers.investment.IProjectMapper;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.capital.ICapitalAllocationRepository;
import com.tnh.baseware.core.repositories.investment.IIndustryRepository;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tnh.baseware.core.services.investment.IProjectService;
import com.tnh.baseware.core.services.investment.capital.imp.CapitalAllocationService;
import com.tnh.baseware.core.specs.GenericSpecification;
import com.tnh.baseware.core.specs.SearchRequest;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectService extends
        GenericService<Project, ProjectEditorForm, ProjectDTO, IProjectRepository, IProjectMapper, UUID>
        implements
        IProjectService {

    IProjectRepository repository;
    IProjectMapper mapper;
    IOrganizationRepository organizationRepository;
    ICategoryRepository categoryRepository;
    IIndustryRepository industryRepository;
    ICapitalAllocationRepository capitalAllocationRepository;
    GenericEntityFetcher fetcher;

    public ProjectService(IProjectRepository repository,
            IProjectMapper mapper,
            IIndustryRepository industryRepository,
            ICategoryRepository categoryRepository,
            IOrganizationRepository organizationRepository,
            ICapitalAllocationRepository capitalAllocationRepository,
            MessageService messageService, CapitalAllocationService capitalAllocationsService, GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, Project.class);
        this.repository = repository;
        this.organizationRepository = organizationRepository;
        this.categoryRepository = categoryRepository;
        this.industryRepository = industryRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
        this.capitalAllocationRepository = capitalAllocationRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO create(ProjectEditorForm form) {
        var project = mapper.formToEntity(form, fetcher, industryRepository, categoryRepository,
                organizationRepository);
        return mapper.entityToDTO(repository.save(project));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDTO update(UUID id, ProjectEditorForm form) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        mapper.updateProjectFromForm(form, project, fetcher, industryRepository, categoryRepository,
                organizationRepository);
        return mapper.entityToDTO(repository.save(project));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id) {
        var project = repository.findById(id)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        
        capitalAllocationRepository.deleteAll(capitalAllocationRepository.findByProjectId(id));

        repository.delete(project);
    }

    public boolean approve(UUID id) {
        Project project = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        project.setIsApproved(true);
        repository.save(project);
        return true;
    }

    public boolean reject(UUID id) {
        Project project = repository.findById(id).orElseThrow(
                                () -> new BWCNotFoundException(messageService.getMessage("project.not.found", id)));
        project.setIsApproved(false);
        repository.save(project);
        return true;
    }

    public List<ProjectCheckAprroveDTO> getListCheckApprove()
    {
        List<Project> projects = repository.findAllByDeletedFalse();
        List<ProjectCheckAprroveDTO> projectDTOs =  mapper.toListProjectCheckAprroveDTOs(projects);
        return projectDTOs;
    }

    public List<ProjectDTO> getListProjectByOwners(List<UUID> ownerIds) {
        List<ProjectDTO> listProjectDTOs = new ArrayList();
        
        if (ownerIds == null || ownerIds.isEmpty()) {
            return listProjectDTOs;
        }
        
        return mapper.entitiesToDTOs(repository.getListProjectByOwners(ownerIds));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {

        Page<Project> projects = repository.findAll(pageable);

        return projects.map(mapper::entityToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll() {

        List<Project> projects = repository.findAll();

        return mapper.entitiesToDTOs(projects);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> search(SearchRequest searchRequest) {
        var specification = new GenericSpecification<Project>(searchRequest);
        var pageable = GenericSpecification.getPageable(searchRequest.getPage(), searchRequest.getSize());

        Page<Project> projects = repository.findAll(specification, pageable);

        return projects.map(mapper::entityToDTO);
    }
}
