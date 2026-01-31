package org.example.service.domain;

import jakarta.persistence.EntityManager;
import org.example.model.dto.response.StudyProgramResponse;
import org.example.model.mapper.StudyProgramMapper;
import org.example.repository.AcademicYearRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.dto.response.CourseResponse;
import org.example.model.entity.*;
import org.example.model.mapper.CourseMapper;
import org.example.repository.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final EntityManager entityManager;
    private final StudyProgramMapper studyProgramMapper;
    private final AcademicYearRepository academicYearRepository;

    public CourseResponse createCourse(CourseCreateRequest request) {
        var course = new CourseEntity();
        var lecturer = entityManager.getReference(LecturerEntity.class, request.lecturerId());
        var studyProgram = entityManager.getReference(StudyProgramEntity.class, request.studyProgramId());
        var academicYear = academicYearRepository.getByActiveTrue()
                        .orElseThrow(()->new IllegalArgumentException("No academic year found"));

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
    }

    public List<StudyProgramResponse> getStudyProgramsForLecturer(Long lecturerId) {
        return repository.findDistinctStudyProgramByLecturerId(lecturerId)
                .stream()
                .map(studyProgramMapper::toStudyProgramResponse)
                .toList();
    }
}
