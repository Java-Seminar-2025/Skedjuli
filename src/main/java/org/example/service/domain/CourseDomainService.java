package org.example.service.domain;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseInfo;
import org.example.model.dto.CourseReadRequestDto;
import org.example.model.entity.CourseEntity;
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
        return courseMapper.toCourseReadRequestDto(courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("course not found")));
    }
}
