package com.tnh.baseware.core.mappers.investment.bid;

import com.tnh.baseware.core.components.GenericEntityFetcher;
import com.tnh.baseware.core.dtos.investment.bid.PackageBidDTO;
import com.tnh.baseware.core.entities.investment.bid.PackageBid;
import com.tnh.baseware.core.forms.investment.bid.PackageBidEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;
import com.tnh.baseware.core.repositories.audit.ICategoryRepository;
import com.tnh.baseware.core.repositories.investment.IProjectRepository;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPackageBidMapper extends IGenericMapper<PackageBid, PackageBidEditorForm, PackageBidDTO> {

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "biddingMethod", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingMethodId()))")
        @Mapping(target = "selectionProcedure", expression = "java(fetcher.formToEntity(categoryRepository, form.getSelectionProcedureId()))")
        @Mapping(target = "contractType", expression = "java(fetcher.formToEntity(categoryRepository, form.getContractTypeId()))")
        @Mapping(target = "biddingField", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingFieldId()))")
        @Mapping(target = "biddingForm", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingFormId()))")
        PackageBid formToEntity(PackageBidEditorForm form,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context ICategoryRepository categoryRepository);

        @Mapping(target = "project", expression = "java(fetcher.formToEntity(projectRepository, form.getProjectId()))")
        @Mapping(target = "biddingMethod", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingMethodId()))")
        @Mapping(target = "selectionProcedure", expression = "java(fetcher.formToEntity(categoryRepository, form.getSelectionProcedureId()))")
        @Mapping(target = "contractType", expression = "java(fetcher.formToEntity(categoryRepository, form.getContractTypeId()))")
        @Mapping(target = "biddingField", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingFieldId()))")
        @Mapping(target = "biddingForm", expression = "java(fetcher.formToEntity(categoryRepository, form.getBiddingFormId()))")
        void updatePackageBidFromForm(PackageBidEditorForm form, @MappingTarget PackageBid packageBid,
                        @Context GenericEntityFetcher fetcher,
                        @Context IProjectRepository projectRepository,
                        @Context ICategoryRepository categoryRepository);
       
}
