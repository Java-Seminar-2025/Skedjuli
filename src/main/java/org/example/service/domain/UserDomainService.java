package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.model.dto.RegisterRequest;
import org.example.model.dto.UserDto;
import org.example.model.dto.UserInfo;
import org.example.model.entity.UserEntity;
import org.example.model.enums.Role;
import org.example.model.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Long createUser(RegisterRequest req, String encodedPassword, Role role) {
        var user = new UserEntity();
        user.setEmail(req.email());
        user.setUsername(generateUsername(req.firstName(),  req.lastName()));
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDateOfBirth(req.dateOfBirth());

        var saved = userRepository.save(user);
        return saved.getId();
    }

    public UserDto getUserDtoByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDto(user);
    }

    public UserInfo getUserInfoByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserInfo(user);
    }

    private String generateUsername(String firstName, String lastName) {
        String prefix = (firstName.substring(0, 1) + lastName.substring(0, 1)).toLowerCase();

        return userRepository
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

}
