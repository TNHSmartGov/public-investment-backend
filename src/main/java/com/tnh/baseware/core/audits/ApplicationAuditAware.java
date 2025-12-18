package com.tnh.baseware.core.audits;

import com.tnh.baseware.core.entities.user.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {

    private static final String DEFAULT_AUDITOR = "starcom";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAuthenticationInvalid(authentication)) {
            return Optional.of(DEFAULT_AUDITOR);
        }

        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Optional.ofNullable(userDetails.getUsername());
    }

    private boolean isAuthenticationInvalid(Authentication authentication) {
        return authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken;
    }
}
