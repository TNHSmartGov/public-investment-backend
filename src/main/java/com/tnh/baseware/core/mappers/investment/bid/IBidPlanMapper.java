package com.tnh.baseware.core.mappers.investment.bid;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.bid.BidPlanDTO;
import com.tnh.baseware.core.entities.investment.bid.BidPlan;
import com.tnh.baseware.core.forms.investment.bid.BidPlanEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.adu.IOrganizationRepository;
import com.tnh.baseware.core.repositories.doc.IFileDocumentRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBidPlanMapper extends IGenericMapper<BidPlan, BidPlanEditorForm, BidPlanDTO> {

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        BidPlan formToEntity(BidPlanEditorForm form,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "authorityOrg", expression = "java(fetcher.formToEntity(organizationRepository, form.getAuthorityOrgId()))")
        @Mapping(target = "fileDocument", expression = "java(fetcher.formToEntity(fileDocumentRepository, form.getFileDocumentId()))")
        void updateBidPlanFromForm(BidPlanEditorForm form, @MappingTarget BidPlan bidPlan,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context IOrganizationRepository organizationRepository,
                        @Context IFileDocumentRepository fileDocumentRepository);

}
