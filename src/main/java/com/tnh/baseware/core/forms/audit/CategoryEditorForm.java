package com.tnh.baseware.core.forms.audit;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class CategoryEditorForm {

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{code.not.blank}")
    @Schema(description = "Represents the category code based on the enum 'CategoryCode'")
    String code;

    @NotBlank(message = "{display.name.not.blank}")
    String displayName;
    String description;
}
