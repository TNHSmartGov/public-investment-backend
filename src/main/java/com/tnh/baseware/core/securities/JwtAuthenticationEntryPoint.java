package com.tnh.baseware.core.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnh.baseware.core.dtos.user.ApiMessageDTO;
import com.tnh.baseware.core.services.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final int UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Map<Class<? extends AuthenticationException>, String> ERROR_CODES = Map.of(
            BadCredentialsException.class, "auth.bad.credentials",
            AccountExpiredException.class, "auth.account.expired",
            DisabledException.class, "auth.account.disabled",
            LockedException.class, "auth.account.locked",
            CredentialsExpiredException.class, "auth.credentials.expired",
            InsufficientAuthenticationException.class, "auth.insufficient.authentication",
            InternalAuthenticationServiceException.class, "auth.internal.authentication.service",
            AuthenticationServiceException.class, "auth.authentication.service",
            AuthenticationCredentialsNotFoundException.class, "auth.credentials.not.found",
            AuthenticationException.class, "auth.authentication.failed");
    MessageService messageService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        var errorCode = ERROR_CODES.getOrDefault(authException.getClass(), "auth.authentication.failed");

        var apiMessageDTO = ApiMessageDTO.<String>builder()
                .data(authException.getMessage())
                .result(false)
                .message(messageService.getMessage(errorCode))
                .code(UNAUTHORIZED)
                .build();

        response.setStatus(UNAUTHORIZED);
        response.setContentType("application/json");

        try (var writer = response.getWriter()) {
            OBJECT_MAPPER.writeValue(writer, apiMessageDTO);
        }
    }
}
