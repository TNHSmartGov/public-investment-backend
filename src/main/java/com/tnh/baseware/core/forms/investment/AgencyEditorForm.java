package com.tnh.baseware.core.forms.investment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
public class AgencyEditorForm {

    @NotNull(message = "{code.not.null}")
    String code;

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{phone.not.blank}")
    String phone;

    @NotBlank(message = "{taxnumber.not.blank}")
    String taxNumber;

    @NotBlank(message = "{address.not.blank}")
    String address;

    String description;
}
