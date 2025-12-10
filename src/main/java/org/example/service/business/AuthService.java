package org.example.service.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AuthRequest;
import org.example.domain.dto.AuthResponse;
import org.example.domain.dto.RegisterRequest;
import org.example.domain.dto.UserDto;
import org.example.domain.enums.Role;
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

        Role role = Role.fromString(req.role());
        var encodedPassword = passwordEncoder.encode(req.password());

        // domain service returns userId (no entity escapes)
        var userId = userDomainService.createUser(req, encodedPassword, role.getValue());

        if (role == Role.STUDENT) {
            studentDomainService.createStudent(userId, (long) req.studyProgramId(), req.enrollmentYear(), req.currentYear());
        } else {
            lecturerDomainService.createLecturer(userId, req.department(), req.academicTitle(), req.officeLocation(), req.phoneNumber());
        }

        return new AuthResponse(jwtService.generateToken(req.email()), req.email());
    }

    public AuthResponse login(AuthRequest req) {
        // get DTO from domain (not entity)
        UserDto userDto = userDomainService.getUserDtoByEmail(req.email());
        loginValidator.validatePassword(userDto, req.password());

        return new AuthResponse(jwtService.generateToken(userDto.email()), userDto.email());
    }
}
