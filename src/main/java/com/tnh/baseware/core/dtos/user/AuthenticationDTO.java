package com.tnh.baseware.core.dtos.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationDTO {

    String accessToken;
    String refreshToken;
    String idToken;
    String tokenType;
    String scope;
    Long expiresIn;
}
