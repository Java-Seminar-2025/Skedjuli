package org.example.validator;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidator {

    private final PasswordEncoder passwordEncoder;

    public void validatePassword(UserDto userDto, String rawPassword) {
        if (userDto == null) throw new RuntimeException("User not found");
        if (!passwordEncoder.matches(rawPassword, userDto.passwordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}
