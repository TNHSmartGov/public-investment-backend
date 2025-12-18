package com.tnh.baseware.core.dtos.adu;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CommuneDTO extends RepresentationModel<CommuneDTO> implements Identifiable<UUID> {

    UUID id;
    String name;
    String code;
    String provinceCode;
    String longitude;
    String latitude;
}
