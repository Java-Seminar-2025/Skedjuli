package org.example.service;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest req) {

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

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(req.getEmail())) {  // using email as username
            throw new RuntimeException("Username already exists");
        }

        int role = "professor".equalsIgnoreCase(req.getRole()) ? 2 : 1;  // 2 = lecturer, 1 = student

        User user = new User();
        user.setUsername(req.getEmail());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        Optional.ofNullable(req.getDateOfBirth()).ifPresent(user::setDateOfBirth);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User savedUser = userRepository.save(user);

        switch (role) {
            case 1 -> {
                StudyProgram studyProgram = studyProgramRepository
                        .findById(req.getStudyProgramId())
                        .orElseThrow(() -> new RuntimeException("Study program not found"));

                Student student = new Student();
                student.setUser(savedUser);
                student.setStudyProgram(studyProgram);
                student.setEnrollmentYear(req.getEnrollmentYear());
                student.setCurrentYear(req.getCurrentYear());
                student.setIsActive(true);
                student.setCreatedAt(now);
                studentRepository.save(student);
            }
            case 2 -> {
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
            default -> throw new RuntimeException("Invalid role");
        }

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
