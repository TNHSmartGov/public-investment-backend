package com.tnh.baseware.core.forms.investment;

import java.util.UUID;
import java.util.Date;

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
public class OtherDecisionEditorForm {

    @NotNull(message = "{code.not.null}")
    String code;

    @NotNull(message = "{name.not.null}")
    String name;    

    @NotNull(message = "{type.not.null}")
    String type;

    @NotNull(message = "{numberNo.not.null}")
    String numberNo;

    @NotNull(message = "{signDate.not.null}")
    Date signDate;

    @NotNull(message = "{projects.not.null}")
    @Schema(description = "Values are retrieved from 'projects'")
    UUID projectId;

    @NotNull(message = "{organizations.not.null}")
    @Schema(description = "Values are retrieved from 'organizations'")
    UUID authorityOrgId;

    @NotNull(message = "{file-documents.not.null}")
    @Schema(description = "Values are retrieved from 'file-documents'")
    UUID fileDocumentId;
   
    String summary;
    
    String description;

}
