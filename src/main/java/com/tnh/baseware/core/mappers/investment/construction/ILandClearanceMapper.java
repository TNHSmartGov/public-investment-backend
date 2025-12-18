package com.tnh.baseware.core.mappers.investment.construction;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.construction.LandClearanceDTO;
import com.tnh.baseware.core.entities.investment.construction.LandClearance;
import com.tnh.baseware.core.forms.investment.construction.LandClearanceEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ILandClearanceMapper extends IGenericMapper<LandClearance, LandClearanceEditorForm, LandClearanceDTO> {

    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
    LandClearance formToEntity(LandClearanceEditorForm form,
                    @Context GenericEntityFetcher fetcher,
                    @Context IProjectRepository projectRepository,
                    @Context IFileDocumentRepository fileDocumentRepository);

    @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
    @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
    void updateLandClearanceFromForm(LandClearanceEditorForm form, @MappingTarget LandClearance landClearance,
                    @Context GenericEntityFetcher fetcher,
                    @Context IProjectRepository projectRepository,
                    @Context IFileDocumentRepository fileDocumentRepository);

}