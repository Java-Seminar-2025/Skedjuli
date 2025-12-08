package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.entity.*;
import org.example.domain.enums.EnrollmentFormItemStatus;
import org.example.domain.enums.EnrollmentFormStatus;
import org.example.repository.AcademicYearRepository;
import org.example.repository.CourseRepository;
import org.example.repository.EnrollmentFormRepository;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EnrollmentFormDomainService {

    private final EnrollmentFormRepository enrollmentFormRepository;
    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final CourseRepository courseRepository;

    public EnrollmentFormEntity createEmptyForm(Long studentId, Long academicYearId, int semester) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        AcademicYearEntity year = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new RuntimeException("Academic year not found"));

        EnrollmentFormEntity f = new EnrollmentFormEntity();
        f.setStudent(student);
        f.setAcademicYear(year);
        f.setSemester(semester);
        f.setStatusEnum(EnrollmentFormStatus.PENDING);
        f.setCreatedAt(LocalDateTime.now());

        return enrollmentFormRepository.save(f);
    }

    public void addItem(EnrollmentFormEntity form, Long courseId) {
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        EnrollmentFormItemEntity item = new EnrollmentFormItemEntity();
        item.setEnrollmentForm(form);
        item.setCourse(course);
        item.setStatusEnum(EnrollmentFormItemStatus.PENDING);
        item.setCreatedAt(LocalDateTime.now());

        form.getItems().add(item);
    }

    public boolean isEnrolledForSemester(Long studentId, Integer semester) {
        return enrollmentFormRepository.existsByStudent_IdAndSemester(studentId, semester);
    }

    public Long getEnrollmentFormId(Long studentId, Integer semester) {
        return enrollmentFormRepository.findByStudent_IdAndSemester(studentId, semester)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found"))
                .getId();
    }

    public void save(EnrollmentFormEntity form) {
        enrollmentFormRepository.save(form);
    }
}