package com.tnh.baseware.core.services.investment.construction.imp;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.construction.ConstructionSite;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.services.investment.construction.IConstructionSiteService;
import com.tnh.baseware.core.dtos.investment.construction.ConstructionSiteDTO;
import com.tnh.baseware.core.forms.investment.construction.ConstructionSiteEditorForm;
import com.tnh.baseware.core.mappers.investment.construction.IConstructionSiteMapper;
import com.tnh.baseware.core.repositories.adu.ICommuneRepository;
import com.tnh.baseware.core.repositories.adu.IProvinceRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.investment.construction.IConstructionSiteRepository;
import com.tnh.baseware.core.services.GenericService;
import com.tnh.baseware.core.services.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConstructionSiteService extends
                GenericService<ConstructionSite, ConstructionSiteEditorForm, ConstructionSiteDTO, IConstructionSiteRepository, IConstructionSiteMapper, UUID>
                implements IConstructionSiteService {


    IConstructionSiteRepository repository;
    IProvinceRepository provinceRepository;
    ICommuneRepository communeRepository;
    IProjectRepository projectRepository;
    IConstructionSiteMapper mapper;
    GenericEntityFetcher fetcher;

    public ConstructionSiteService(IConstructionSiteRepository repository,
            IProvinceRepository provinceRepository,
            ICommuneRepository communeRepository,
            IProjectRepository projectRepository,
            IConstructionSiteMapper mapper,
            MessageService messageService,
            GenericEntityFetcher fetcher) {
        super(repository, mapper, messageService, ConstructionSite.class);

        this.repository = repository;
        this.provinceRepository = provinceRepository;
        this.communeRepository = communeRepository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
        this.fetcher = fetcher;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ConstructionSiteDTO create(ConstructionSiteEditorForm form) {
            var constructionSite = mapper.formToEntity(form, fetcher, provinceRepository, communeRepository, projectRepository);
            return mapper.entityToDTO(repository.save(constructionSite));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ConstructionSiteDTO update(UUID id, ConstructionSiteEditorForm form) {
            var constructionSite = repository.findById(id)
                    .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("construction.site.not.found", id)));
            mapper.updateConstructionSiteFromForm(form, constructionSite, fetcher, provinceRepository, communeRepository, projectRepository);
            return mapper.entityToDTO(repository.save(constructionSite));
    }
}
