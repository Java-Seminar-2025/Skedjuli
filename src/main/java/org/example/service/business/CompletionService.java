package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CompletedCourseCreateRequest;
import org.example.model.dto.CompletedCourseDto;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CompletedCourseDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.StudentDomainService;
import org.example.service.validator.CompletedCourseValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompletionService {

    private final CompletedCourseDomainService completedCourseDomainService;
    private final StudentDomainService studentDomainService;
    private final CourseDomainService courseDomainService;
    private final AcademicYearDomainService academicYearDomainService;
    private final CompletedCourseValidator completedCourseValidator;

    @Transactional
    public CompletedCourseDto markCompleted(CompletedCourseCreateRequest request) {
        completedCourseValidator.validateCreate(request);

        studentDomainService.getCurrentYearById(request.studentId());
        courseDomainService.getCourseInfoById(request.courseId());

        Long academicYearId = academicYearDomainService.getActiveYearId();

        return completedCourseDomainService.createCompletedCourse(
                request.studentId(),
                request.courseId(),
                request.grade(),
                request.completionDate(),
                academicYearId
        );
    }
}