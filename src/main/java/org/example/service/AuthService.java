package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

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

    //regiseer
    @Transactional
    public AuthResponse register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.email)) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(req.email)) {
            throw new RuntimeException("Username already exists");
        }

        //   professor -> LECTURER, else STUDENT
        User.Role role = "professor".equalsIgnoreCase(req.role)
                ? User.Role.lecturer
                : User.Role.student;

        // create user
        User u = new User();
        u.setUsername(req.email);
        u.setEmail(req.email);
        u.setFirstName(req.firstName);
        u.setLastName(req.lastName);
        u.setRole(role);
        u.setPassword(passwordEncoder.encode(req.password));
        if (req.dateOfBirth != null && !req.dateOfBirth.isBlank()) {
            try {
                u.setDateOfBirth(LocalDate.parse(req.dateOfBirth)); // "yyyy-MM-dd"
            } catch (Exception ignored) { /* ostavi null ako format nije dobar */ }
        }
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(u);

        //student
        if (role == User.Role.student) {
            Student s = new Student();
            s.userId = saved.getId();
            s.studyProgramId = req.studyProgramId;
            s.enrollmentYear = req.enrollmentYear;
            s.currentYear = req.currentYear;
            s.isActive = true;
            studentRepository.save(s);
        } else {
            // lecturer
            Lecturer l = new Lecturer();
            l.userId = saved.getId();
            l.department = req.department;
            l.academicTitle = req.academicTitle;
            l.officeLocation = req.officeLocation;
            l.phoneNumber = req.phoneNumber;
            l.isActive = true;
            lecturerRepository.save(l);
        }

        //jwt
        String token = jwtService.generateToken(saved.getEmail());
        return new AuthResponse(token, saved.getEmail());


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
