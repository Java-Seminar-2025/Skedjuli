package org.example.service.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AuthRequest;
import org.example.domain.dto.AuthResponse;
import org.example.domain.dto.RegisterRequest;
import org.example.domain.entity.UserEntity;
import org.example.service.domain.LecturerDomainService;
import org.example.service.domain.StudentDomainService;
import org.example.service.domain.StudyProgramDomainService;
import org.example.service.domain.UserDomainService;
import org.example.service.infrastructure.JwtService;
import org.example.validator.LoginValidator;
import org.example.validator.RegisterValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@RequiredArgsConstructor
public class AuthService {

    private final RegisterValidator registerValidator;
    private final LoginValidator loginValidator;
    private final UserDomainService userDomainService;
    private final StudentDomainService studentDomainService;
    private final LecturerDomainService lecturerDomainService;
    private final StudyProgramDomainService studyProgramDomainService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest req) {

        registerValidator.validate(req);

        if (userDomainService.existsByEmail(req.email()))
            throw new RuntimeException("Email already exists");

        if (userDomainService.existsByUsername(req.email()))
            throw new RuntimeException("Username already exists");

        int role = req.role().equalsIgnoreCase("professor") ? 2 : 1;
        String encodedPassword = passwordEncoder.encode(req.password());

        UserEntity user = userDomainService.createUser(req, encodedPassword, role);

        if (role == 1) {
            studentDomainService.createStudent(
                    user,
                    studyProgramDomainService.getById(req.studyProgramId()),
                    req.enrollmentYear(),
                    req.currentYear()
            );
        } else {
            lecturerDomainService.createLecturer(
                    user,
                    req.department(),
                    req.academicTitle(),
                    req.officeLocation(),
                    req.phoneNumber()
            );
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()), user.getEmail());
    }

    public AuthResponse login(AuthRequest req) {
        UserEntity user = loginValidator.validate(req.email(), req.password());

        return new AuthResponse(
                jwtService.generateToken(user.getEmail()),
                user.getEmail()
        );
    }
}
