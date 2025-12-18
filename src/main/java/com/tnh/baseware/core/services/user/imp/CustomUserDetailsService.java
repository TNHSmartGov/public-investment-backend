package com.tnh.baseware.core.services.user.imp;

import com.tnh.baseware.core.entities.user.CustomUserDetails;
import com.tnh.baseware.core.entities.user.User;
import com.tnh.baseware.core.exceptions.BWCNotFoundException;
import com.tnh.baseware.core.repositories.user.IUserRepository;
import com.tnh.baseware.core.services.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {

    IUserRepository userRepository;
    MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BWCNotFoundException(messageService.getMessage("user.not.found", username)));

        UserDetails userDetails = CustomUserDetails.builder()
                .user(user)
                .build();
        return userDetails;
    }
}
