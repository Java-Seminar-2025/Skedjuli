package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.entity.StudentEntity;
import org.example.domain.entity.StudyProgramEntity;
import org.example.domain.entity.UserEntity;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class StudentDomainService {

    private final StudentRepository studentRepository;

    public void createStudent(UserEntity user, StudyProgramEntity studyProgram, int enrollmentYear, int currentYear) {
        StudentEntity student = new StudentEntity();
        student.setUser(user);
        student.setStudyProgram(studyProgram);
        student.setEnrollmentYear(enrollmentYear);
        student.setCurrentYear(currentYear);
        student.setIsActive(true);
        student.setCreatedAt(LocalDateTime.now());

        studentRepository.save(student);
    }

    public StudyProgramEntity getStudyProgramById(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getStudyProgram();
    }

    public Integer getCurrentYearById(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getCurrentYear();
    }
}