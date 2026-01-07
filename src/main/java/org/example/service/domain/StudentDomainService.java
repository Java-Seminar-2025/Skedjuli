package org.example.service.domain;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.StudentPatchRequest;
import org.example.model.dto.StudentResponse;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.entity.UserEntity;
import org.example.repository.StudentRepository;
import org.example.repository.specification.StudentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public void patchStudent(Long studentId, StudentPatchRequest request) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        if (request.currentYear() != null) {
            student.setCurrentYear(request.currentYear());
        }

        if (request.isActive() != null) {
            student.setIsActive(request.isActive());
        }
    }

    public StudentResponse getStudent(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        return new StudentResponse(
                student.getEnrollmentYear(),
                student.getCurrentYear(),
                student.getAverageGrade(),
                student.getTotalEctsEarned(),
                student.getIsActive()
        );
    }

    public void deleteStudent(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        studentRepository.delete(student);
    }

    public Page<StudentResponse> getStudents(int page, int size, String sortBy, String sortOrder, Long studyProgramId, Integer enrollmentYear, Integer currentYear, Double totalEctsEarned, Boolean isActive) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StudentEntity> students = studentRepository.findAll(
                StudentSpecification.filter(studyProgramId, enrollmentYear, currentYear, totalEctsEarned, isActive), pageable
        );
        return students.map(this::toResponse);
    }

    private StudentResponse toResponse(StudentEntity student) {
        return new StudentResponse(
                student.getEnrollmentYear(),
                student.getCurrentYear(),
                student.getAverageGrade(),
                student.getTotalEctsEarned(),
                student.getIsActive()
        );
    }
}