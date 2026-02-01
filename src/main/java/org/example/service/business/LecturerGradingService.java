package org.example.service.business;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseGradeCreateRequest;
import org.example.model.dto.response.CompletedCourseResponse;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CompletedCourseEntity;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.model.mapper.CompletedCourseMapper;
import org.example.repository.CompletedCourseRepository;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.domain.CourseDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerGradingService {
    private final AcademicYearDomainService academicYearDomainService;
    private final CourseDomainService courseDomainService;
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;
    private final CompletedCourseRepository completedCourseRepository;
    private final CompletedCourseMapper completedCourseMapper;
    private final EntityManager entityManager;
    @Transactional
    public CompletedCourseResponse upsertGrade(CourseGradeCreateRequest request) {
                var activeYear = academicYearDomainService.getActiveAcademicYear();

                        var courseOpt = courseDomainService.getCourse(request.courseId());
                if (courseOpt.isEmpty()) {
                        throw new IllegalArgumentException("Course not found (id=" + request.courseId() + ")");
                    }

                        var course = courseOpt.get();
                if (course.getLecturer() == null || course.getLecturer().getId() == null) {
                        throw new IllegalStateException("Course has no lecturer assigned");
                    }
                if (!course.getLecturer().getId().equals(request.lecturerId())) {
                        throw new IllegalStateException("Lecturer is not assigned to this course");
                    }
                var validStatuses = List.of(EnrollmentFormStatus.APPROVED.getValue());

                        boolean enrolled = enrollmentFormItemRepository.existsEnrollmentForStudentCourseYearStatuses(
                                request.studentId(),
                                request.courseId(),
                                activeYear.id(),
                                validStatuses
                                );

                        if (!enrolled) {
                        throw new IllegalStateException("Student is not enrolled on this course in the active academic year");
                    }

                        var completionDate = request.completionDate() != null ? request.completionDate() : LocalDate.now();

                        var existing = completedCourseRepository.findByStudent_IdAndCourse_Id(request.studentId(), request.courseId());
                if (existing.isPresent()) {
                        var entity = existing.get();
                        entity.setGrade(request.grade());
                        entity.setCompletionDate(completionDate);
                        if (entity.getAcademicYear() == null) {
                                entity.setAcademicYear(entityManager.getReference(AcademicYearEntity.class, activeYear.id()));
                            }
                        return completedCourseMapper.toResponse(entity);
                    }

                        var completed = new CompletedCourseEntity();
                completed.setStudent(entityManager.getReference(org.example.model.entity.StudentEntity.class, request.studentId()));
                completed.setCourse(entityManager.getReference(org.example.model.entity.CourseEntity.class, request.courseId()));
                completed.setAcademicYear(entityManager.getReference(AcademicYearEntity.class, activeYear.id()));
                completed.setGrade(request.grade());
                completed.setCompletionDate(completionDate);

                        var saved = completedCourseRepository.save(completed);
                return completedCourseMapper.toResponse(saved);
            }
}
