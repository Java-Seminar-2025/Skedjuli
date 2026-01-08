package org.example.service.domain;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.StudentCreateRequest;
import org.example.model.dto.request.patch.StudentPatchRequest;
import org.example.model.dto.StudentResponse;
import org.example.model.entity.StudentEntity;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.entity.UserEntity;
import org.example.model.mapper.StudentMapper;
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
    private final StudentMapper studentMapper;

    public void createStudent(StudentCreateRequest request) {
        var student = new StudentEntity();

        var uRef = new UserEntity();
        uRef.setId(request.userId());
        student.setUser(uRef);

        var spRef = new StudyProgramEntity();
        spRef.setId(request.studyProgramId());
        student.setStudyProgram(spRef);

        student.setEnrollmentYear(request.enrollmentYear());
        student.setCurrentYear(request.currentYear());
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
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        if (request.currentYear() != null) {
            student.setCurrentYear(request.currentYear());
        }

        if (request.isActive() != null) {
            student.setIsActive(request.isActive());
        }
    }

    public StudentResponse getStudent(Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        return  studentMapper.toStudentResponse(student);
    }

    public void deleteStudent(Long studentId) {
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(studentId)));

        studentRepository.delete(student);
    }

    public Page<StudentResponse> getStudents(int page, int size, String sortBy, String sortOrder, Long studyProgramId, Integer enrollmentYear, Integer currentYear, Double totalEctsEarned, Boolean isActive) {
        var sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        var students = studentRepository.findAll(
                StudentSpecification.filter(studyProgramId, enrollmentYear, currentYear, totalEctsEarned, isActive), pageable
        );

        return students.map(studentMapper::toStudentResponse);
    }
}