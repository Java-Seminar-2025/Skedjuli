package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.entity.EnrollmentFormEntity;
import org.example.domain.entity.EnrollmentFormItemEntity;
import org.example.domain.entity.CourseEntity;
import org.example.domain.entity.StudentEntity;
import org.example.domain.entity.AcademicYearEntity;
import org.example.domain.enums.EnrollmentFormItemStatus;
import org.example.domain.enums.EnrollmentFormStatus;
import org.example.repository.EnrollmentFormRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EnrollmentFormDomainService {

    private final EnrollmentFormRepository enrollmentFormRepository;

    public Long createEmptyFormReturnId(Long studentId, Long academicYearId, int semester) {
        var f = new EnrollmentFormEntity();

        var studentRef = new StudentEntity();
        studentRef.setId(studentId);
        f.setStudent(studentRef);

        var yearRef = new AcademicYearEntity();
        yearRef.setId(academicYearId);
        f.setAcademicYear(yearRef);

        f.setSemester(semester);
        f.setStatusEnum(EnrollmentFormStatus.PENDING);
        f.setCreatedAt(LocalDateTime.now());

        var saved = enrollmentFormRepository.save(f);
        return saved.getId();
    }

    public void addItemByFormId(Long enrollmentFormId, Long courseId) {
        var form = enrollmentFormRepository.findById(enrollmentFormId)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found"));

        var item = new EnrollmentFormItemEntity();
        item.setEnrollmentForm(form);

        var courseRef = new CourseEntity();
        courseRef.setId(courseId);
        item.setCourse(courseRef);

        item.setStatusEnum(EnrollmentFormItemStatus.PENDING);
        item.setCreatedAt(LocalDateTime.now());

        form.getItems().add(item);
        enrollmentFormRepository.save(form);
    }

    public Long getEnrollmentFormId(Long studentId, Integer semester) {
        return enrollmentFormRepository.findByStudent_IdAndSemester(studentId, semester)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found"))
                .getId();
    }
}
