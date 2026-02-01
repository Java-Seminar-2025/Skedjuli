package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CourseResponse;
import org.example.model.dto.response.SemesterCoursesResponse;
import org.example.model.dto.response.StudentCourseWithStatusResponse;
import org.example.model.enums.EnrollmentFormStatus;
import org.example.model.enums.StudentCourseStatus;
import org.example.repository.CourseRepository;
import org.example.service.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentCourseOverviewService {
    private final StudentDomainService studentDomainService;
    private final AcademicYearDomainService academicYearDomainService;
    private final CompletedCourseDomainService completedCourseDomainService;
    private final EnrollmentFormItemDomainService enrollmentFormItemDomainService;
    private final CourseRepository courseRepository;

    public List<SemesterCoursesResponse> getCoursesBySemesterWithStatus(Long studentId) {
        var studyProgramId = studentDomainService.getStudyProgramIdByStudentId(studentId);
        var activeYear = academicYearDomainService.getActiveAcademicYear();
        var allCourses = courseRepository.findByStudyProgram_IdAndAcademicYear_ActiveTrueAndActiveTrueOrderBySemesterAsc(studyProgramId);
        var completedIds = completedCourseDomainService.getCompletedCourseIdSet(studentId);
        var enrolledIds = enrollmentFormItemDomainService.findEnrolledCoursesForStudent(studentId, EnrollmentFormStatus.APPROVED.getValue(), activeYear.id())
                .stream()
                .map(CourseResponse::id)
                .collect(Collectors.toSet());

        var withStatus = allCourses.stream()
                .map(c-> {
                            var courseResp = new CourseResponse(
                                    c.getId(), c.getCode(), c.getName(), c.getDescription(), c.getEcts(), c.getMandatory(), c.getEnrollmentLimit(),
                                    c.getLecturer() == null ? null : c.getLecturer().getId(),
                                    c.getStudyProgram() == null ? null : c.getStudyProgram().getId(),
                                    c.getAcademicYear() == null ? null : c.getAcademicYear().getId(),
                                    c.getSemester(), c.getActive(), Set.of()
                            );

                            StudentCourseStatus status =
                                    completedIds.contains(c.getId()) ? StudentCourseStatus.COMPLETED :
                                            enrolledIds.contains(c.getId()) ? StudentCourseStatus.ENROLLED :
                                                    StudentCourseStatus.AVAILABLE;
                            return new StudentCourseWithStatusResponse(courseResp, status);
                        })
                .toList();
        Map<Integer, List<StudentCourseWithStatusResponse>> grouped =
                withStatus.stream().collect(Collectors.groupingBy(r -> r.course().semester()));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new SemesterCoursesResponse(e.getKey(), e.getValue()))
                .toList();
    }

    public List<SemesterCoursesResponse> getCompletedCoursesBySemester(Long studentId) {
        return filterByStatus(studentId, StudentCourseStatus.COMPLETED);
    }

    public List<SemesterCoursesResponse> getEnrolledCoursesBySemester(Long studentId) {
        return filterByStatus(studentId, StudentCourseStatus.ENROLLED);
    }

    public List<SemesterCoursesResponse> getAvailableCoursesBySemester(Long studentId) {
        return filterByStatus(studentId, StudentCourseStatus.AVAILABLE);
    }

    private List<SemesterCoursesResponse> filterByStatus(Long studentId, StudentCourseStatus status) {
        return getCoursesBySemesterWithStatus(studentId)
                .stream()
                .map(semester -> new SemesterCoursesResponse(
                        semester.semester(),
                        semester.courses()
                                .stream()
                                .filter(c -> c.status() == status)
                                .toList()
                ))
                .filter(semester -> !semester.courses().isEmpty())
                .toList();
    }
}
