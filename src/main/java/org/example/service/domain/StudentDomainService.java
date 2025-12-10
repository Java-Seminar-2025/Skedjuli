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

    public void createStudent(Long userId, Long studyProgramId, int enrollmentYear, int currentYear) {
        var student = new StudentEntity();

        var uRef = new UserEntity();
        uRef.setId(userId);
        student.setUser(uRef);

        var spRef = new StudyProgramEntity();
        spRef.setId(studyProgramId);
        student.setStudyProgram(spRef);

        student.setEnrollmentYear(enrollmentYear);
        student.setCurrentYear(currentYear);
        student.setIsActive(true);
        student.setCreatedAt(LocalDateTime.now());

        studentRepository.save(student);
    }

    public Long getStudyProgramIdByStudentId(Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getStudyProgram().getId();
    }

    public Integer getCurrentYearById(Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getCurrentYear();
    }
}
