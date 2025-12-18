package com.tnh.baseware.core.mappers.investment;

import com.tnh.baseware.core.dtos.investment.AgencyDTO;
import com.tnh.baseware.core.entities.investment.Agency;
import com.tnh.baseware.core.forms.investment.AgencyEditorForm;
import com.tnh.baseware.core.mappers.IGenericMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAgencyMapper extends IGenericMapper<Agency, AgencyEditorForm, AgencyDTO> {

    Agency formToEntity(AgencyEditorForm form);

    void updateAgencyFromForm(AgencyEditorForm form, @MappingTarget Agency agency);

    @Mapping(source = "parent", target = "parent", qualifiedByName = "mapParent")
    AgencyDTO entityToDTO(Agency entity);

    @Named("mapParent")
    default AgencyDTO mapParent(Agency parent) {
        if (parent == null)
            return null;

        var parentDTO = AgencyDTO.builder()
                .id(parent.getId())
                .code(parent.getCode())
                .name(parent.getName())
                .phone(parent.getPhone())
                .taxNumber(parent.getTaxNumber())
                .address(parent.getAddress())
                .description(parent.getDescription());

        return parentDTO.build();
    }

    default List<AgencyDTO> mapAgencysToTree(List<Agency> agencys) {
        if (agencys == null || agencys.isEmpty())
            return List.of();

        var parentMap = agencys.stream()
                .filter(m -> m.getParent() != null)
                .collect(Collectors.groupingBy(m -> m.getParent().getId()));

        return agencys.stream()
                .filter(m -> m.getParent() == null)
                .map(m -> buildAgencyTree(m, parentMap))
                .toList();
    }

    default AgencyDTO buildAgencyTree(Agency m, Map<UUID, List<Agency>> parentMap) {
        var dto = entityToDTO(m);
        List<Agency> children = parentMap.getOrDefault(m.getId(), List.of());

        if (!children.isEmpty()) {
            var childDTOs = children.stream()
                    .map(child -> buildAgencyTree(child, parentMap))
                    .toList();
            dto.setChildren(childDTOs);
        }

        return dto;
    }
}
