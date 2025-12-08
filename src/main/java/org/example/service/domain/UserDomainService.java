package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.dto.RegisterRequest;
import org.example.domain.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(RegisterRequest req, String encodedPassword, int role) {
        UserEntity user = new UserEntity();
        user.setEmail(req.email());
        user.setUsername(req.email());
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDateOfBirth(req.dateOfBirth());

        return userRepository.save(user);
    }

}