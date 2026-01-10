package org.example.service.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
        return repository.existsByEmail(email);
    }

    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        var user = new UserEntity();
        user.setEmail(request.email());
        user.setUsername(generateUsername(request.firstName(), request.lastName()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPassword(encodedPassword);
        user.setRole(Role.fromString(request.role()));
        user.setDateOfBirth(request.dateOfBirth());

        var saved = repository.save(user);
        return mapper.toUserResponse(saved);
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

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.firstName() != null || request.lastName() != null) {
            user.setUsername(generateUsername(
                    request.firstName() != null ? request.firstName() : user.getFirstName(),
                    request.lastName() != null ? request.lastName() : user.getLastName()
            ));
        }
        if (request.email() != null) {
            if (!user.getEmail().equals(request.email()) && existsByEmail(request.email())) {
                throw new IllegalArgumentException("Email already exists: " + request.email());
            }
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.dateOfBirth() != null) user.setDateOfBirth(request.dateOfBirth());

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
        return repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }
}
