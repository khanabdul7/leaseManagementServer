package com.example.lease_management.security;

import com.example.lease_management.entity.User;
import com.example.lease_management.Repos.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        String email = authentication.getName();

        return userRepository.findByUsername(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found in DB"));
    }
}

