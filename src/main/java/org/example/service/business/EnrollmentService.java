package org.example.service.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.dto.EnrollmentCourseResponse;
import org.example.domain.entity.EnrollmentFormEntity;
import org.example.domain.entity.EnrollmentFormItemEntity;
import org.example.domain.mapper.CourseMapper;
import org.example.service.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class EnrollmentService {

    private final CourseDomainService courseDomainService;
    private final StudentDomainService studentDomainService;
    private final AcademicYearDomainService academicYearDomainService;
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final CompletedCourseDomainService completedCourseDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;

    private void enrollSemester(Long studentId, int semester) {
        if (enrollmentFormDomainService.isEnrolledForSemester(studentId, semester)) {
            throw new RuntimeException("Already enrolled");
        }

        Long activeYearId = academicYearDomainService.getActiveYear().getId();
        Long studyProgramId = studentDomainService.getStudyProgramById(studentId).getId();

        EnrollmentFormEntity form = enrollmentFormDomainService.createEmptyForm(studentId, activeYearId, semester);

        List<Long> mandatoryCourseIds = courseDomainService.getMandatoryCourseIds(studyProgramId, semester);
        mandatoryCourseIds.stream()
                .filter(courseId -> !completedCourseDomainService.hasCompletedCourse(studentId, courseId))
                .forEach(courseId -> enrollmentFormDomainService.addItem(form, courseId));

        enrollmentFormDomainService.save(form);
    }

    public void enrollYear(Long studentId) {
        int year = studentDomainService.getCurrentYearById(studentId);
        enrollSemester(studentId, year * 2 - 1);
        enrollSemester(studentId, year * 2);
    }

    public List<EnrollmentCourseResponse> getEnrolledCoursesForYear(Long studentId) {

        int year = studentDomainService.getCurrentYearById(studentId);
        int sem1 = year * 2 - 1;
        int sem2 = year * 2;

        List<Long> formIds = List.of(
                enrollmentFormDomainService.getEnrollmentFormId(studentId, sem1),
                enrollmentFormDomainService.getEnrollmentFormId(studentId, sem2)
        );

        return formIds.stream()
                .flatMap(formId -> enrollmentFormItemDomainService.getEnrollmentFormItems(formId).stream())
                .map(EnrollmentFormItemEntity::getCourse)
                .map(CourseMapper::toDto)
                .toList();
    }
}