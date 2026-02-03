package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CourseGradeResponse;
import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.response.StudentAnalyticsResponse;
import org.example.model.entity.CompletedCourseEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.model.mapper.CourseMapper;
import org.example.repository.AcademicYearRepository;
import org.example.repository.CompletedCourseRepository;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAnalyticsService {

    private final StudentRepository studentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final CompletedCourseRepository completedCourseRepository;
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;
    private final CourseMapper courseMapper;

    public StudentAnalyticsResponse getStudentAnalytics(Long studentId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        var activeYear = academicYearRepository.getByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active academic year found"));

        Long activeYearId = activeYear.getId();

        Double avgThisYear = normalize(completedCourseRepository.avgForStudentInAcademicYear(studentId, activeYearId));
        Double avgOverall = normalize(completedCourseRepository.avgOverallForStudent(studentId));

        Long studyProgramId = student.getStudyProgram().getId();
        Integer enrollmentYear = student.getEnrollmentYear();

        Double cohortAvgThisYear = normalize(
                completedCourseRepository.avgForCohortInAcademicYear(studyProgramId, enrollmentYear, activeYearId)
        );
        Double cohortAvgOverall = normalize(
                completedCourseRepository.avgOverallForCohort(studyProgramId, enrollmentYear)
        );

        List<CompletedCourseEntity> completed = completedCourseRepository.findByStudent_Id(studentId);

        List<CourseGradeResponse> gradedCourses = completed.stream()
                .filter(cc -> cc.getGrade() != null)
                .map(cc -> new CourseGradeResponse(
                        cc.getCourse().getId(),
                        cc.getCourse().getCode(),
                        cc.getCourse().getName(),
                        cc.getGrade(),
                        cc.getAcademicYear() != null ? cc.getAcademicYear().getId() : null,
                        cc.getAcademicYear() != null ? cc.getAcademicYear().getYearCode() : null
                ))
                .sorted(Comparator
                        .comparing((CourseGradeResponse c) -> c.academicYearCode() == null ? "" : c.academicYearCode())
                        .thenComparing(CourseGradeResponse::courseName))
                .toList();

        List<CourseEntity> enrolledActiveYear = enrollmentFormItemRepository.findEnrolledCoursesForStudent(
                studentId,
                EnrollmentFormStatus.APPROVED.getValue(),
                activeYearId
        );

        Set<Long> gradedThisYearCourseIds = completed.stream()
                .filter(cc -> cc.getGrade() != null)
                .filter(cc -> cc.getAcademicYear() != null && Objects.equals(cc.getAcademicYear().getId(), activeYearId))
                .map(cc -> cc.getCourse().getId())
                .collect(Collectors.toSet());

        List<CourseResponse> notPassed = enrolledActiveYear.stream()
                .filter(c -> !gradedThisYearCourseIds.contains(c.getId()))
                .map(courseMapper::toCourseResponse)
                .sorted(Comparator.comparing(CourseResponse::name))
                .toList();

        Double deltaThisYear = normalize(
                (avgThisYear != null && cohortAvgThisYear != null) ? avgThisYear - cohortAvgThisYear : null
                );
        Double deltaOverall = normalize(
                (avgOverall != null && cohortAvgOverall != null) ? avgOverall - cohortAvgOverall : null
        );

        return new StudentAnalyticsResponse(
                studentId,
                avgThisYear,
                avgOverall,
                cohortAvgThisYear,
                cohortAvgOverall,
                deltaThisYear,
                deltaOverall,
                gradedCourses,
                notPassed
        );
    }

    private Double normalize(Double value) {
        if (value == null) return null;
        return Math.round(value * 100.0) / 100.0;
    }
}
