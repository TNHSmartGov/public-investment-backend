package com.tnh.baseware.core.forms.investment.capital;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CapitalEditorForm {
    
    @NotNull(message = "{code.not.null}")
    String code;

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{name.not.blank}")
    String shortName;

    String description;

    @Schema(description = "Cấp quản lý: CAP_TINH, CAP_XA", example = "CAP_TINH")
    String level;

}
