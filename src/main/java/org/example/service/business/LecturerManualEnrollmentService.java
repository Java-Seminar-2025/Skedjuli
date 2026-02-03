package org.example.service.business;

import lombok.AllArgsConstructor;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.example.model.enums.EnrollmentFormItemStatus;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LecturerManualEnrollmentService {
    private final CourseDomainService courseDomainService;
    private final AcademicYearDomainService academicYearDomainService;
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;

    @Transactional
    public void addStudentToMyCourse(Long lecturerId, Long courseId, Long studentId) {
        if (lecturerId == null || courseId == null || studentId == null) {
            throw new IllegalArgumentException("lecturerId, courseId and studentId are required");
        }

        var course = courseDomainService.getCourse(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id=" + courseId));

        var courseLecturer = course.getLecturer();
        if (courseLecturer == null || courseLecturer.getId() == null || !courseLecturer.getId().equals(lecturerId)) {
            throw new SecurityException("Lecturer is not assigned to this course");
        }

        var activeYear = academicYearDomainService.getActiveAcademicYear();
        if (activeYear == null || activeYear.id() == null) {
            throw new IllegalStateException("No active academic year found");
        }

        if (course.getAcademicYear() == null || course.getAcademicYear().getId() == null) {
            throw new IllegalStateException("Course has no academic year assigned");
        }
        if (!course.getAcademicYear().getId().equals(activeYear.id())) {
            throw new IllegalStateException("Course is not in the active academic year");
        }

        var semester = course.getSemester();
        if (semester == null) {
            throw new IllegalStateException("Course has no semester assigned");
        }

        var formIdOpt = enrollmentFormDomainService
                .findFormIdByStudentAndAcademicYearAndSemester(studentId, activeYear.id(), semester);

        if (formIdOpt.isEmpty()) {
            throw new IllegalStateException("Student has no enrollment form for this semester/year");
        }

        var formId = formIdOpt.get();

        if (!enrollmentFormDomainService.isLocked(formId)) {
            throw new IllegalStateException("Student enrollment form is not locked yet");
        }

        if (enrollmentFormItemRepository.existsByEnrollmentForm_IdAndCourse_Id(formId, courseId)) {
            return;
        }

        var item = new EnrollmentFormItemEntity();

        var formRef = new EnrollmentFormEntity();
        formRef.setId(formId);
        item.setEnrollmentForm(formRef);

        var courseRef = new CourseEntity();
        courseRef.setId(courseId);
        item.setCourse(courseRef);

        item.setStatusEnum(EnrollmentFormItemStatus.PENDING);

        enrollmentFormItemRepository.save(item);
    }
}