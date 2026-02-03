package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.UserDto;
import org.example.model.dto.request.create.UserCreateRequest;
import org.example.model.dto.request.patch.UserPatchRequest;
import org.example.model.dto.response.UserResponse;
import org.example.model.entity.UserEntity;
import org.example.model.enums.Role;
import org.example.model.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public Boolean existsByEmail(String email) {
        return repository.existsByEmailIgnoreCase(email);
    }

    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        var email = request.email().trim().toLowerCase();
        var firstName = request.firstName().trim();
        var lastName = request.lastName().trim();
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        var user = new UserEntity();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(encodedPassword);
        user.setRole(Role.fromString(request.role()));
        user.setDateOfBirth(request.dateOfBirth());

        assignUniqueUsernameWithRetry(user, firstName,  lastName);
        return mapper.toUserResponse(user);
    }

    public UserDto getUserDtoByEmail(String email) {
        return mapper.toUserDto(getUserOrThrow(email));
    }

    public UserResponse getUserResponseByEmail(String email) {
        return mapper.toUserResponse(getUserOrThrow(email));
    }

    @Transactional
    public UserResponse patchUser(String email, UserPatchRequest request) {
        var user = getUserOrThrow(email);

        if (request.firstName() != null) user.setFirstName(request.firstName().trim());
        if (request.lastName() != null) user.setLastName(request.lastName().trim());
        if (request.email() != null) {
            var newEmail = request.email().trim().toLowerCase();
            if (!user.getEmail().equals(newEmail) && existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email already exists: " + newEmail);
            }
            user.setEmail(newEmail);
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());
        if (request.firstName() != null || request.lastName() != null) {
            assignUniqueUsernameWithRetry(user, user.getFirstName(), user.getLastName());
        }
        else {
            repository.saveAndFlush(user);
        }

        return mapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(String email) {
        var user = getUserOrThrow(email);
        repository.delete(user);
    }

    private String generateUsername(String firstName, String lastName) {
        String prefix = (firstName.substring(0, 1) + lastName.substring(0, 1)).toLowerCase();

        return repository
                .findTopByUsernameStartingWithOrderByUsernameDesc(prefix)
                .map(user -> {
                    String lastUsername = user.getUsername();

                    if (lastUsername.length() > prefix.length()) {
                        try {
                            String numberPart = lastUsername.substring(prefix.length());

                            int lastNumber = Integer.parseInt(numberPart);

                            return prefix + String.format("%07d", lastNumber + 1);
                        } catch (NumberFormatException e) {
                            return prefix + "0000000";
                        }
                    } else {
                        return prefix + "0000000";
                    }
                })
                .orElse(prefix + "0000000");
    }

    private UserEntity getUserOrThrow(String email) {
        var normalized = email == null ? "" : email.trim().toLowerCase();
        return repository.findByEmail(normalized)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + normalized));
    }

    private void assignUniqueUsernameWithRetry(UserEntity user, String firstName, String lastName) {
        var maxAttempts = 20;
        for (int i = 0; i < maxAttempts; i++) {
            user.setUsername(generateUsername(firstName, lastName));
            try {
                repository.saveAndFlush(user);
                return;
            } catch (DataIntegrityViolationException ignored) {
            }
        }
        throw new IllegalStateException("Could not generate unique username");
    }
}
