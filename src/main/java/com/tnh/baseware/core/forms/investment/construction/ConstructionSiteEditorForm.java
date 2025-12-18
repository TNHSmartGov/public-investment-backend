package com.tnh.baseware.core.forms.investment.construction;

import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ConstructionSiteEditorForm {

    @NotNull(message = "{project.not.null}")
    UUID projectId;

    @NotNull(message = "{provice.not.null}")
    @Schema(description = "Values are retrieved from 'provinces'")
    UUID provinceId;

    @Schema(description = "Values are retrieved from 'communes'")
    UUID communeId;
}
