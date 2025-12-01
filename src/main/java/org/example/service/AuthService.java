package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.repository.LecturerRepository;
import org.example.repository.StudentRepository;
import org.example.repository.StudyProgramRepository;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest req) {

        // Basic validation
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Password cannot be empty");
        }
        if (req.getFirstName() == null || req.getFirstName().isBlank() ||
                req.getLastName() == null || req.getLastName().isBlank()) {
            throw new RuntimeException("First name and last name are required");
        }

        // Duplicate checks
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(req.getEmail())) {
            throw new RuntimeException("Username already exists");
        }


        int role = "professor".equalsIgnoreCase(req.getRole()) ? 2 : 1;

        LocalDateTime now = LocalDateTime.now();

        // Create user
        User user = new User();
        user.setUsername(req.getEmail());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        Optional.ofNullable(req.getDateOfBirth()).ifPresent(user::setDateOfBirth);

        User savedUser = userRepository.save(user);

        // Create related entity
        if (role == 1) {
            // STUDENT
            StudyProgram studyProgram = studyProgramRepository.findById(req.getStudyProgramId())
                    .orElseThrow(() -> new RuntimeException("Study program not found"));

            Student student = new Student();
            student.setUser(savedUser);
            student.setStudyProgram(studyProgram);
            student.setEnrollmentYear(req.getEnrollmentYear());
            student.setCurrentYear(req.getCurrentYear());
            student.setIsActive(true);
            student.setCreatedAt(now);

            studentRepository.save(student);

        } else if (role == 2) {
            // LECTURER
            Lecturer lecturer = new Lecturer();
            lecturer.setUser(savedUser);
            lecturer.setDepartment(req.getDepartment());
            lecturer.setAcademicTitle(req.getAcademicTitle());
            lecturer.setOfficeLocation(req.getOfficeLocation());
            lecturer.setPhoneNumber(req.getPhoneNumber());
            lecturer.setIsActive(true);
            lecturer.setCreatedAt(now);

            lecturerRepository.save(lecturer);
        }

        // Generate JWT
        String token = jwtService.generateToken(savedUser.getEmail());
        return new AuthResponse(token, savedUser.getEmail());
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .or(() -> userRepository.findByUsername(authRequest.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }
}