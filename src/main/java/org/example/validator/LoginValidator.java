package org.example.validator;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.UserEntity;
import org.example.service.domain.UserDomainService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidator {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    public UserEntity validate(String email, String rawPassword) {
        UserEntity user = userDomainService.getByEmail(email);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}