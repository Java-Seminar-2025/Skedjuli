package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.example.model.dto.CourseCreateRequest;
import org.example.model.dto.CourseDto;
import org.example.model.dto.CourseInfo;
import org.example.model.dto.CourseReadRequestDto;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.entity.UserEntity;
=======
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.dto.response.CourseResponse;
import org.example.model.entity.*;
>>>>>>> origin/enrollment
import org.example.model.mapper.CourseMapper;
import org.example.repository.AcademicYearRepository;
import org.example.repository.CourseRepository;
<<<<<<< HEAD
import org.example.repository.LecturerRepository;
import org.example.repository.StudyProgramRepository;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
=======

>>>>>>> origin/enrollment
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final EntityManager entityManager;

    public CourseResponse createCourse(CourseCreateRequest request) {
        var course = new CourseEntity();
        var lecturer = entityManager.getReference(LecturerEntity.class, request.lecturerId());
        var studyProgram = entityManager.getReference(StudyProgramEntity.class, request.studyProgramId());
        var academicYear = entityManager.getReference(AcademicYearEntity.class, request.academicYearId());

        course.setCode(request.code());
        course.setName(request.name());
        course.setDescription(request.description());
        course.setEcts(request.ects());
        course.setMandatory(request.mandatory());
        course.setEnrollmentLimit(request.enrollmentLimit());
        course.setLecturer(lecturer);
        course.setStudyProgram(studyProgram);
        course.setAcademicYear(academicYear);
        course.setSemester(request.semester());
        course.setActive(request.active());

        if (request.prerequisiteCourseIds() != null) {
            var prereqs = request.prerequisiteCourseIds().stream()
                    .map(id -> entityManager.getReference(CourseEntity.class, id))
                    .collect(Collectors.toSet());
            course.setPrerequisites(prereqs);
        }

        var saved = repository.save(course);
        return mapper.toCourseResponse(saved);
    }

    private final UserRepository userRepository;
    private final LecturerRepository lecturerRepository;
    private final StudyProgramRepository studyProgramRepository;
    private final AcademicYearRepository academicYearRepository;

    public List<Long> getMandatoryCourseIds(Long studyProgramId, Integer semester) {
        return repository.findByStudyProgram_IdAndSemesterAndMandatoryTrue(studyProgramId, semester)
                .stream()
                .map(CourseEntity::getId)
                .toList();
    }

    public List<CourseResponse> getMandatoryCoursesForSemesters(Long studyProgramId, List<Integer> semesters) {
        return semesters.stream()
                .flatMap(sem -> repository.findByStudyProgram_IdAndSemesterAndMandatoryTrue(studyProgramId, sem).stream())
                .distinct()
                .map(mapper::toCourseResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> getSelectableCoursesForYear(Long studyProgramId, int year) {
        var semStart = (year - 1) * 2 + 1;
        var sems = List.of(semStart, semStart + 1);
        return sems.stream()
                .flatMap(sem -> repository.findByStudyProgram_IdAndSemesterAndMandatoryFalse(studyProgramId, sem).stream())
                .distinct()
                .map(mapper::toCourseResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long courseId) {
        return mapper.toCourseResponse(getCourseOrThrow(courseId));
    }

<<<<<<< HEAD
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
=======
    @Transactional
    public CourseResponse patchCourse(Long courseId, CoursePatchRequest request) {
        var course = getCourseOrThrow(courseId);

        if (request.code() != null) course.setCode(request.code());
        if (request.name() != null) course.setName(request.name());
        if (request.description() != null) course.setDescription(request.description());
        if (request.ects() != null) course.setEcts(request.ects());
        if (request.semester() != null) course.setSemester(request.semester());
        if (request.mandatory() != null) course.setMandatory(request.mandatory());
        if (request.active() != null) course.setActive(request.active());
        if (request.enrollmentLimit() != null) course.setEnrollmentLimit(request.enrollmentLimit());

        if (request.studyProgramId() != null) {
            course.setStudyProgram(
                    entityManager.getReference(StudyProgramEntity.class, request.studyProgramId())
            );
        }

        if (request.academicYearId() != null) {
            course.setAcademicYear(
                    entityManager.getReference(AcademicYearEntity.class, request.academicYearId())
            );
        }

        if (request.lecturerId() != null) {
            course.setLecturer(
                    entityManager.getReference(LecturerEntity.class, request.lecturerId())
            );
        }

        return mapper.toCourseResponse(course);
    }

    public void deleteCourse(Long courseId) {
        repository.delete(getCourseOrThrow(courseId));
    }

    private CourseEntity getCourseOrThrow(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("courseId is null");
        }

        return repository.findById(courseId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Course not found with id: " + courseId)
                );
>>>>>>> origin/enrollment
    }
}
