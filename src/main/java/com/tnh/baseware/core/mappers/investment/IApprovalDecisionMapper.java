package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.dtos.investment.ApprovalDecisionDTO;
import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.entities.investment.ApprovalDecision;
import com.tnh.baseware.core.forms.investment.ApprovalDecisionEditorForm;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.mappers.IGenericMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IApprovalDecisionMapper extends IGenericMapper<ApprovalDecision, ApprovalDecisionEditorForm, ApprovalDecisionDTO> {

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        @Mapping(target = "startDate", source = "startDate", qualifiedByName = "stringToYearDate")
        @Mapping(target = "endDate", source = "endDate", qualifiedByName = "stringToYearDate")
        ApprovalDecision formToEntity(ApprovalDecisionEditorForm form,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        @Mapping(target = "startDate", source = "startDate", qualifiedByName = "stringToYearDate")
        @Mapping(target = "endDate", source = "endDate", qualifiedByName = "stringToYearDate")
        void updateApprovalDecisionFromForm(ApprovalDecisionEditorForm form, @MappingTarget ApprovalDecision approvalDecision,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

        @Named("stringToYearDate")
        static Date stringToYearDate(String year) {
                if (year == null) return null;
                try {
                return new SimpleDateFormat("yyyy").parse(year);
                } catch (ParseException e) {
                throw new RuntimeException("Invalid year format: " + year);
                }
        }

}