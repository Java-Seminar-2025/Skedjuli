package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.AuthRequest;
import org.example.model.dto.AuthResponse;
import org.example.model.dto.request.create.UserCreateRequest;
import org.example.model.dto.request.create.LecturerCreateRequest;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.model.enums.Role;
import org.example.service.domain.LecturerDomainService;
import org.example.service.domain.StudentDomainService;
import org.example.service.domain.StudyProgramDomainService;
import org.example.service.domain.UserDomainService;
import org.example.service.validator.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Getter
@AllArgsConstructor
public class AuthService {

    private final UserDomainService userDomainService;
    private final StudentDomainService studentDomainService;
    private final LecturerDomainService lecturerDomainService;
    private final StudyProgramDomainService studyProgramDomainService;
    private final PasswordEncoder passwordEncoder;
    private final AuthValidator authValidator;
    private final EnrollmentService enrollmentService;

    @Transactional
    public AuthResponse register(UserCreateRequest request) {
        // validate input; will throw EnrollmentValidationException on failure
        authValidator.validateRegister(request);

        var role = Role.fromString(request.role());


        // create user via domain service (domain service returns userId)
        var user = userDomainService.createUser(request);

        // create domain-specific entity for student/lecturer using domain services
        if (role == Role.STUDENT) {
            var enrollmentYear = Optional.ofNullable(request.enrollmentYear())
                    .orElse(LocalDate.now().getYear());

            // studyProgramId may be int/long depending on your DTO — cast if needed
            var studentCreateRequest = new StudentCreateRequest(user.id(), request.studyProgramId(), enrollmentYear, 1);
            var createdStudent = studentDomainService.createStudent(studentCreateRequest);
            enrollmentService.enrollYear(createdStudent.id());
        } else {
            var lecturerCreateRequest = new LecturerCreateRequest(user.id(), request.department(), request.academicTitle(), request.officeLocation(), request.phoneNumber());
            lecturerDomainService.createLecturer(lecturerCreateRequest);
        }

        return new AuthResponse(request.email());
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

        return new AuthResponse(userDto.email());
    }
}
