package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.AuthRequest;
import org.example.model.dto.AuthResponse;
import org.example.model.dto.RegisterRequest;
import org.example.model.dto.request.create.LecturerCreateRequest;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.enums.Role;
import org.example.service.domain.LecturerDomainService;
import org.example.service.domain.StudentDomainService;
import org.example.service.domain.StudyProgramDomainService;
import org.example.service.domain.UserDomainService;
import org.example.service.infrastructure.JwtService;
import org.example.service.validator.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@AllArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;
    private final StudentDomainService studentDomainService;
    private final LecturerDomainService lecturerDomainService;
    private final StudyProgramDomainService studyProgramDomainService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthValidator authValidator;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        // validate input; will throw EnrollmentValidationException on failure
        authValidator.validateRegister(req);

        var role = Role.fromString(req.role());
        var encodedPassword = passwordEncoder.encode(req.password());

        // create user via domain service (domain service returns userId)
        var userId = userDomainService.createUser(req, encodedPassword, role);

        // create domain-specific entity for student/lecturer using domain services
        if (role == Role.STUDENT) {
            // studyProgramId may be int/long depending on your DTO — cast if needed
            var studentCreateRequest = new StudentCreateRequest(userId, req.studyProgramId(), req.enrollmentYear(), req.currentYear());
            studentDomainService.createStudent(studentCreateRequest);
        } else {
            var lecturerCreateRequest = new LecturerCreateRequest(userId, req.department(), req.academicTitle(), req.officeLocation(), req.phoneNumber());
            lecturerDomainService.createLecturer(lecturerCreateRequest);
        }

        var token = jwtService.generateToken(req.email());
        return new AuthResponse(token, req.email());
    }

    /**
     * Login: validate parameters, verify password via AuthValidator, then return token.
     */
    public AuthResponse login(AuthRequest req) {
        // basic param validation (throws EnrollmentValidationException if invalid)
        authValidator.validateLogin(req.email(), req.password());

        // fetch user DTO from domain
        var userDto = userDomainService.getUserDtoByEmail(req.email());

        // check password (throws EnrollmentValidationException on failure)
        authValidator.validatePassword(userDto, req.password());

        var token = jwtService.generateToken(userDto.email());
        return new AuthResponse(token, userDto.email());
    }
}
