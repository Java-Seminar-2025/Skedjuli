package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.enums.EnrollmentFormItemStatus;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.repository.EnrollmentFormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Domain service owning EnrollmentFormRepository only.
 * Does not depend on other domain services or repositories.
 */
@Service
@AllArgsConstructor
public class EnrollmentFormDomainService {

    private final EnrollmentFormRepository enrollmentFormRepository;

    /**
     * Create a minimal enrollment form (student/academic-year references by id) and return its id.
     */
    @Transactional
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

    /**
     * Find a form id by student + academicYear + semester.
     */
    @Transactional(readOnly = true)
    public Optional<Long> findFormIdByStudentAndAcademicYearAndSemester(Long studentId, Long academicYearId, int semester) {
        return enrollmentFormRepository
                .findTopByStudent_IdAndAcademicYear_IdAndSemesterOrderByIdDesc(studentId, academicYearId, semester)
                .map(EnrollmentFormEntity::getId);
    }

    /**
     * Find existing form id or create and return a new one (only uses EnrollmentFormRepository).
     */
    @Transactional
    public Long findOrCreateFormId(Long studentId, Long academicYearId, int semester) {
        return findFormIdByStudentAndAcademicYearAndSemester(studentId, academicYearId, semester)
                .orElseGet(() -> createEmptyFormReturnId(studentId, academicYearId, semester));
    }

    /**
     * Return enrollment form id for given student and semester or throw if not found.
     */
    @Transactional(readOnly = true)
    public Long getEnrollmentFormId(Long studentId, int semester) {
        return enrollmentFormRepository.findTopByStudent_IdAndSemesterOrderByIdDesc(studentId, semester)
                .map(EnrollmentFormEntity::getId)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found for studentId=" + studentId + " semester=" + semester));
    }

    /**
     * Add an item (course) to the enrollment form identified by enrollmentFormId.
     * This method loads the form entity (so it uses the repository) and persists the added item.
     */
    @Transactional
    public void addItemByFormId(Long enrollmentFormId, Long courseId) {
        var form = enrollmentFormRepository.findById(enrollmentFormId)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found: id=" + enrollmentFormId));

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

    /**
     * Helper: find the current form id for the student's year by checking the two semesters.
     * This method does NOT compute the student's current year — caller must provide semester start.
     *
     * @param studentId      student id
     * @param academicYearId active academic year id
     * @param semStart       semester start (e.g. 1, 3, 5)
     * @return semStart form id if exists, otherwise semStart+1 form id if exists, otherwise empty Optional
     */
    @Transactional(readOnly = true)
    public Optional<Long> findCurrentFormIdForStudent(Long studentId, Long academicYearId, int semStart) {
        return findFormIdByStudentAndAcademicYearAndSemester(studentId, academicYearId, semStart)
                .or(() -> findFormIdByStudentAndAcademicYearAndSemester(studentId, academicYearId, semStart + 1));
    }

    @Transactional
    public void approveForm(Long enrollmentFormId) {
        var form = enrollmentFormRepository.findById(enrollmentFormId)
                .orElseThrow(() -> new RuntimeException("Enrollment form not found: id=" + enrollmentFormId));

        form.setStatusEnum(EnrollmentFormStatus.APPROVED);
        form.setCreatedAt(LocalDateTime.now());

        enrollmentFormRepository.save(form);
    }
}
