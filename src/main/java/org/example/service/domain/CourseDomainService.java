package org.example.service.domain;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseInfo;

import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.entity.*;
import org.example.model.mapper.CourseMapper;
import org.example.repository.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDomainService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final EntityManager entityManager;

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
        if (courseId == null)
            throw new IllegalArgumentException("courseId is null");
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("course not found"));
        return courseMapper.toCourseInfo(course);
    }


    public void createCourse(CourseCreateRequest request) {
        var course = new CourseEntity();

        course.setCode(request.code());
        course.setName(request.name());
        course.setDescription(request.description());
        course.setEcts(request.ects());
        course.setMandatory(request.mandatory());
        course.setEnrollmentLimit(request.enrollmentLimit());

        var lecturer = entityManager.getReference(LecturerEntity.class, request.lecturerId());
        var studyProgram = entityManager.getReference(StudyProgramEntity.class, request.studyProgramId());
        var academicYear = entityManager.getReference(AcademicYearEntity.class, request.academicYearId());

        course.setLecturer(lecturer);
        course.setStudyProgram(studyProgram);
        course.setAcademicYear(academicYear);

        course.setSemester(request.semester());
        course.setActive(request.active());

        courseRepository.save(course);
    }

    @Transactional
    public void patchCourse(Long courseId, CoursePatchRequest request) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(courseId)));

        if (request.code() != null) course.setCode(request.code());
        if (request.name() != null) course.setName(request.name());
        if (request.description() != null) course.setDescription(request.description());
        if (request.ects() != null) course.setEcts(request.ects());
        if (request.semester() != null) course.setSemester(request.semester());
        if (request.mandatory() != null) course.setMandatory(request.mandatory());
        if (request.active() != null) course.setActive(request.active());
        if (request.enrollmentLimit() != null) course.setEnrollmentLimit(request.enrollmentLimit());

        if (request.studyProgramId() != null) {
            var sp = new StudyProgramEntity();
            sp.setId(request.studyProgramId());
            course.setStudyProgram(sp);
        }

        if (request.academicYearId() != null) {
            var ay = new AcademicYearEntity();
            ay.setId(request.academicYearId());
            course.setAcademicYear(ay);
        }

        if (request.lecturerId() != null) {
            var lecturer = new LecturerEntity();
            lecturer.setId(request.lecturerId());
            course.setLecturer(lecturer);
        }
    }

    public void deleteCourse(Long courseId) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(courseId)));
        courseRepository.deleteById(courseId);
    }
}
