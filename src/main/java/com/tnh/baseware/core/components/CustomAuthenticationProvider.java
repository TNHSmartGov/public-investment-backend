package com.tnh.baseware.core.components;

import com.tnh.baseware.core.entities.user.CustomUserDetails;
import com.tnh.baseware.core.exceptions.BWCAuthenticationException;
import com.tnh.baseware.core.securities.LoginAttemptService;
import com.tnh.baseware.core.services.MessageService;
import com.tnh.baseware.core.services.user.imp.CustomUserDetailsService;
import com.tnh.baseware.core.utils.LogStyleHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomAuthenticationProvider implements AuthenticationProvider {

    CustomUserDetailsService userDetailsService;
    PasswordEncoder passwordEncoder;
    LoginAttemptService loginAttemptService;
    MessageService messageService;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) {
        Assert.notNull(authentication, "Authentication object cannot be null");

        var username = authentication.getName();
        var password = (String) authentication.getCredentials();
        var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        // Unlock account if lockout time has expired
        loginAttemptService.unlockWhenTimeExpired(userDetails.getUser());
        validateUserStatus(userDetails);

        // Check password matches
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            loginAttemptService.loginFailed(userDetails.getUser());
            log.debug(LogStyleHelper.debug("Authentication failed for user: {} - Bad credentials"), username);
            throw new BWCAuthenticationException(messageService.getMessage("auth.bad.credentials"));
        }

        // Authentication successful
        loginAttemptService.loginSucceeded(userDetails.getUser());
        log.debug(LogStyleHelper.debug("User {} authenticated successfully"), username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void validateUserStatus(CustomUserDetails userDetails) {
        String username = userDetails.getUsername();

        if (!userDetails.isAccountNonLocked()) {
            log.debug(LogStyleHelper.debug("Authentication failed: Account locked for user: {}"), username);
            throw new BWCAuthenticationException(messageService.getMessage("auth.account.locked"));
        }

        if (!userDetails.isEnabled()) {
            log.debug(LogStyleHelper.debug("Authentication failed: Account disabled for user: {}"), username);
            throw new BWCAuthenticationException(messageService.getMessage("auth.account.disabled"));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

