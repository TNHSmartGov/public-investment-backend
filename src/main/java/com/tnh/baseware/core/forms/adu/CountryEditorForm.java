package com.tnh.baseware.core.forms.adu;

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
public class CountryEditorForm {

    @NotBlank(message = "{name.not.blank}")
    String name;

    @NotBlank(message = "{code.not.blank}")
    String code;
    String longitude;
    String latitude;
}
