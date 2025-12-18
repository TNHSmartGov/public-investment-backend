package com.tnh.baseware.core.dtos.user;

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
public class TenantDTO extends RepresentationModel<TenantDTO> implements Identifiable<UUID> {

    UUID id;
    String name;
    String schemaName;
    Boolean active;
}
