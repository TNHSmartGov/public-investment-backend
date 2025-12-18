package com.tnh.baseware.core.dtos.investment.project;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tnh.baseware.core.entities.audit.Identifiable;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSummaryDTO extends RepresentationModel<ProjectSummaryDTO> implements Identifiable<UUID> {
    UUID id;
    String code;
    String name;
}
