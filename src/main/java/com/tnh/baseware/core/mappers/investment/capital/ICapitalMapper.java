package com.tnh.baseware.core.mappers.investment.capital;

import com.tnh.baseware.core.dtos.investment.capital.CapitalDTO;
import com.tnh.baseware.core.entities.investment.capital.Capital;
import com.tnh.baseware.core.forms.investment.CapitalEditorForm;
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
public interface ICapitalMapper extends IGenericMapper<Capital, CapitalEditorForm, CapitalDTO> {


    Capital formToEntity(CapitalEditorForm form);

    void updateCapitalFromForm(CapitalEditorForm form, @MappingTarget Capital capital);

    @Mapping(source = "parent", target = "parent", qualifiedByName = "mapParent")
    CapitalDTO entityToDTO(Capital entity);

    @Named("mapParent")
    default CapitalDTO mapParent(Capital parent) {
        if (parent == null) return null;

        var parentDTO = CapitalDTO.builder()
                .id(parent.getId())
                .code(parent.getCode())
                .name(parent.getName())
                .shortName(parent.getShortName())
                .description(parent.getDescription());
        
        return parentDTO.build();
    }

    default List<CapitalDTO> mapCapitalsToTree(List<Capital> captitals) {
        if (captitals == null || captitals.isEmpty()) return List.of();

        var parentMap = captitals.stream()
                .filter(m -> m.getParent() != null)
                .collect(Collectors.groupingBy(m -> m.getParent().getId()));

        return captitals.stream()
                .filter(m -> m.getParent() == null)
                .map(m -> buildCapitalTree(m, parentMap))
                .toList();
    }

    default CapitalDTO buildCapitalTree(Capital m, Map<UUID, List<Capital>> parentMap) {
        var dto = entityToDTO(m);
        List<Capital> children = parentMap.getOrDefault(m.getId(), List.of());

        if (!children.isEmpty()) {
            var childDTOs = children.stream()
                    .map(child -> buildCapitalTree(child, parentMap))
                    .toList();
            dto.setChildren(childDTOs);
        }

        return dto;
    }
}
