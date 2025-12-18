package com.tnh.baseware.core.dtos.investment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.investment.IndustryDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndustryDTO extends RepresentationModel<IndustryDTO> implements Identifiable<UUID> {

    UUID id;
    String code;
    String name;
    String description;
    
}
