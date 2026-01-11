package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseCreateRequest;
import org.example.model.dto.CourseDto;
import org.example.model.dto.CourseInfo;
import org.example.model.dto.CourseReadRequestDto;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.entity.UserEntity;
import org.example.model.mapper.CourseMapper;
import org.example.repository.AcademicYearRepository;
import org.example.repository.CourseRepository;
import org.example.repository.LecturerRepository;
import org.example.repository.StudyProgramRepository;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final UserRepository userRepository;
    private final LecturerRepository lecturerRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final AcademicYearRepository academicYearRepository;

    public List<Long> getMandatoryCourseIds(Long studyProgramId, Integer semester) {
        return courseRepository.findByStudyProgram_IdAndSemesterAndMandatoryTrue(studyProgramId, semester)
                .stream()
                .map(CourseEntity::getId)
                .toList();
    }

    public List<CourseInfo> getMandatoryCoursesForSemesters(Long studyProgramId, List<Integer> semesters) {
        return semesters.stream()
                .flatMap(sem -> courseRepository.findByStudyProgram_IdAndSemesterAndMandatoryTrue(studyProgramId, sem).stream())
                .distinct()
                .map(c -> new CourseInfo(c.getId(), c.getName(), c.getEcts(), c.getSemester()))
                .collect(Collectors.toList());
    }

    public List<CourseInfo> getSelectableCoursesForYear(Long studyProgramId, int year) {
        var semStart = (year - 1) * 2 + 1;
        var sems = List.of(semStart, semStart + 1);
        return sems.stream()
                .flatMap(sem -> courseRepository.findByStudyProgram_IdAndSemesterAndMandatoryFalse(studyProgramId, sem).stream())
                .distinct()
                .map(c -> new CourseInfo(c.getId(), c.getName(), c.getEcts(), c.getSemester()))
                .collect(Collectors.toList());
    }

    public CourseInfo getCourseInfoById(Long courseId) {
        if (courseId == null) throw new IllegalArgumentException("courseId is null");
        var entity = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("course not found"));
        return courseMapper.toCourseInfo(entity);
    }

    public CourseReadRequestDto getCourseReadRequestDtoById(Long courseId) {
        return courseMapper.toCourseReadRequestDto(
                courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("course not found"))
        );
    }

    public CourseDto createCourseAsLecturer(String principal, CourseCreateRequest request) {
        LecturerEntity lecturer = getLecturerByPrincipal(principal);

        StudyProgramEntity studyProgram = studyProgramRepository.findById(request.getStudyProgramId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Study program not found"));

        AcademicYearEntity academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Academic year not found"));

        if (courseRepository.existsByCode(request.getCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Course code already exists");
        }

        CourseEntity course = new CourseEntity();
        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setEcts(request.getEcts());
        course.setMandatory(request.getMandatory());
        course.setEnrollmentLimit(request.getEnrollmentLimit());
        course.setSemester(request.getSemester());
        course.setLecturer(lecturer);
        course.setStudyProgram(studyProgram);
        course.setAcademicYear(academicYear);

        CourseEntity saved = courseRepository.save(course);

        CourseDto dto = new CourseDto();
        dto.setId(saved.getId());
        dto.setCode(saved.getCode());
        dto.setName(saved.getName());
        dto.setDescription(saved.getDescription());
        dto.setEcts(saved.getEcts());
        dto.setSemester(saved.getSemester());
        dto.setMandatory(saved.getMandatory());
        dto.setEnrollmentLimit(saved.getEnrollmentLimit());
        dto.setLecturerId(lecturer.getId());
        dto.setStudyProgramId(studyProgram.getId());
        dto.setAcademicYearId(academicYear.getId());
        return dto;
    }

    public List<CourseDto> getMyCourses(String principal) {
        LecturerEntity lecturer = getLecturerByPrincipal(principal);
        return courseRepository.findByLecturer_Id(lecturer.getId()).stream().map(this::toDto).toList();
    }

    public CourseDto updateMyCourse(String principal, Long courseId, CourseCreateRequest request) {
        LecturerEntity lecturer = getLecturerByPrincipal(principal);
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (course.getLecturer() == null || !course.getLecturer().getId().equals(lecturer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your course");
        }

        StudyProgramEntity studyProgram = studyProgramRepository.findById(request.getStudyProgramId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Study program not found"));

        AcademicYearEntity academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Academic year not found"));

        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setEcts(request.getEcts());
        course.setMandatory(request.getMandatory());
        course.setEnrollmentLimit(request.getEnrollmentLimit());
        course.setSemester(request.getSemester());
        course.setStudyProgram(studyProgram);
        course.setAcademicYear(academicYear);

        CourseEntity saved = courseRepository.save(course);
        return toDto(saved);
    }

    public void deleteMyCourse(String principal, Long courseId) {
        LecturerEntity lecturer = getLecturerByPrincipal(principal);
        CourseEntity course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        if (course.getLecturer() == null || !course.getLecturer().getId().equals(lecturer.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your course");
        }

        courseRepository.delete(course);
    }

    private LecturerEntity getLecturerByPrincipal(String principal) {
        UserEntity user = userRepository.findByEmail(principal)
                .or(() -> userRepository.findByUsername(principal))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        return lecturerRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a lecturer"));
    }

    private CourseDto toDto(CourseEntity c) {
        CourseDto dto = new CourseDto();
        dto.setId(c.getId());
        dto.setCode(c.getCode());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setEcts(c.getEcts());
        dto.setSemester(c.getSemester());
        dto.setMandatory(c.getMandatory());
        dto.setEnrollmentLimit(c.getEnrollmentLimit());
        dto.setLecturerId(c.getLecturer() == null ? null : c.getLecturer().getId());
        dto.setStudyProgramId(c.getStudyProgram() == null ? null : c.getStudyProgram().getId());
        dto.setAcademicYearId(c.getAcademicYear() == null ? null : c.getAcademicYear().getId());
        return dto;
    }
}
