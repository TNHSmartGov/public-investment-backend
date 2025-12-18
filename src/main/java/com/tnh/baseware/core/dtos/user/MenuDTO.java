package com.tnh.baseware.core.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tnh.baseware.core.dtos.audit.CategoryDTO;
import com.tnh.baseware.core.entities.audit.Identifiable;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO extends RepresentationModel<MenuDTO> implements Identifiable<UUID> {

    UUID id;
    String title;
    CategoryDTO menuType;
    String alias;
    String note;
    String path;
    String link;
    Integer published;
    Integer browserNav;
    String icon;
    Integer menuOrder;
    String description;

    MenuDTO parent;
    List<MenuDTO> children;
}
