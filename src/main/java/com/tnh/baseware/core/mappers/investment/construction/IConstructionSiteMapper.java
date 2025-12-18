package com.tnh.baseware.core.mappers.investment.construction;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.construction.ConstructionSiteDTO;
import com.tnh.baseware.core.forms.investment.construction.ConstructionSiteEditorForm;
import com.tnh.baseware.core.entities.investment.construction.ConstructionSite;
import com.tnh.baseware.core.repositories.adu.ICommuneRepository;
import com.tnh.baseware.core.repositories.adu.IProvinceRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.mappers.adu.ICommuneMapper;
import com.tnh.baseware.core.mappers.adu.IProvinceMapper;
import com.tnh.baseware.core.mappers.investment.IProjectMapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
        IProvinceMapper.class,
        ICommuneMapper.class,
        IProjectMapper.class
})
public interface IConstructionSiteMapper extends IGenericMapper<ConstructionSite, ConstructionSiteEditorForm, ConstructionSiteDTO> {

    @Mapping(target = "province", expression = "java(fetcher.formToEntity(provinceRepository, form.getProvinceId()))")
    @Mapping(target = "commune", expression = "java(fetcher.formToEntity(communeRepository, form.getCommuneId()))")
    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    ConstructionSite formToEntity(ConstructionSiteEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context IProvinceRepository provinceRepository,
                    @Context ICommuneRepository communeRepository,
                    @Context IProjectRepository projectRepository);

    @Mapping(target = "province", expression = "java(fetcher.formToEntity(provinceRepository, form.getProvinceId()))")
    @Mapping(target = "commune", expression = "java(fetcher.formToEntity(communeRepository, form.getCommuneId()))")
    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    void updateConstructionSiteFromForm(ConstructionSiteEditorForm form, @MappingTarget ConstructionSite constructionSite,
                    @Context GenericEntityFetcher fetcher,
                    @Context IProvinceRepository provinceRepository,
                    @Context ICommuneRepository communeRepository,
                    @Context IProjectRepository projectRepository);
}
