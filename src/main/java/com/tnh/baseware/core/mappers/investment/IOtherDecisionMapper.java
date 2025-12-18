package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.dtos.investment.OtherDecisionDTO;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.OtherDecision;
import com.tnh.baseware.core.forms.investment.OtherDecisionEditorForm;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.mappers.IGenericMapper;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOtherDecisionMapper extends IGenericMapper<OtherDecision, OtherDecisionEditorForm, OtherDecisionDTO> {

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        OtherDecision formToEntity(OtherDecisionEditorForm form,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        void updateApprovalDecisionFromForm(OtherDecisionEditorForm form, @MappingTarget OtherDecision otherDecision,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

}