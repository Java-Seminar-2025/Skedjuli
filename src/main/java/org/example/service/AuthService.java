package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       LecturerRepository lecturerRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    //register
    @Transactional
    public AuthResponse register(RegisterRequest req) {

        // 1️⃣ Basic validations
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

        // 2️⃣ Check if user exists
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(req.getEmail())) {  // using email as username
            throw new RuntimeException("Username already exists");
        }

        // 3️⃣ Determine role
        int role = "professor".equalsIgnoreCase(req.getRole()) ? 2 : 1;  // 2 = lecturer, 1 = student

        // 4️⃣ Create User
        User user = new User();
        user.setUsername(req.getEmail());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // 4a️⃣ Optional date of birth
        if (req.getDateOfBirth() != null) {
            try {
                user.setDateOfBirth(req.getDateOfBirth());
            } catch (Exception ignored) {
                user.setDateOfBirth(null);
            }
        }

        // 4b️⃣ Timestamps
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User savedUser = userRepository.save(user);

        // 5️⃣ Create Student or Lecturer
        if (role == 1) { // student
            Student student = new Student();
            student.setUserId(savedUser.getId());
            student.setStudyProgramId(req.getStudyProgramId());
            student.setEnrollmentYear(req.getEnrollmentYear());
            student.setCurrentYear(req.getCurrentYear());
            student.setIsActive(true);
            student.setCreatedAt(now);
            studentRepository.save(student);
        } else { // lecturer
            Lecturer lecturer = new Lecturer();
            lecturer.setUserId(savedUser.getId());
            lecturer.setDepartment(req.getDepartment());
            lecturer.setAcademicTitle(req.getAcademicTitle());
            lecturer.setOfficeLocation(req.getOfficeLocation());
            lecturer.setPhoneNumber(req.getPhoneNumber());
            lecturer.setIsActive(true);
            lecturer.setCreatedAt(now);
            lecturerRepository.save(lecturer);
        }

        // 6️⃣ Generate JWT token
        String token = jwtService.generateToken(savedUser.getEmail());
        return new AuthResponse(token, savedUser.getEmail());
    }

    //login
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseGet(() -> userRepository.findByUsername(authRequest.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }
}
