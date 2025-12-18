package com.tnh.baseware.core.forms.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrivilegeEditorForm {

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{resource.name.not.blank}")
    String resourceName;
    String description;

    @NotBlank(message = "{api.pattern.not.blank}")
    String apiPattern;
}
