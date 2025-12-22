package org.example.service.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.CompletedCourseDto;
import org.example.model.entity.AcademicYearEntity;
import org.example.model.entity.CompletedCourseEntity;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.StudentEntity;
import org.example.model.mapper.CompletedCourseMapper;
import org.example.repository.CompletedCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompletedCourseDomainService {

    private final CompletedCourseRepository completedCourseRepository;
    private final EntityManager entityManager;
    private final CompletedCourseMapper completedCourseMapper;

    public boolean hasCompletedCourse(Long studentId, Long courseId) {
        return completedCourseRepository.existsByStudent_IdAndCourse_Id(studentId, courseId);
    }

    public List<Long> getCompletedCourseIds(Long studentId) {
        return completedCourseRepository.findByStudent_Id(studentId)
                .stream()
                .map(cc -> cc.getCourse().getId())
                .collect(Collectors.toList());
    }

    public Set<Long> getCompletedCourseIdSet(Long studentId) {
        var set = completedCourseRepository.findByStudent_Id(studentId)
                .stream()
                .map(cc -> cc.getCourse().getId())
                .collect(Collectors.toCollection(HashSet::new));
        return Collections.unmodifiableSet(set);
    }

    @Transactional
    public CompletedCourseDto createCompletedCourse(Long studentId, Long courseId, int grade, LocalDate completionDate, Long academicYearId) {
        var studentRef = entityManager.getReference(StudentEntity.class, studentId);
        var courseRef = entityManager.getReference(CourseEntity.class, courseId);
        var yearRef = entityManager.getReference(AcademicYearEntity.class, academicYearId);

        var entity = new CompletedCourseEntity();
        entity.setStudent(studentRef);
        entity.setCourse(courseRef);
        entity.setGrade(grade);
        entity.setCompletionDate(completionDate);
        entity.setAcademicYear(yearRef);

        var saved = completedCourseRepository.save(entity);

        return completedCourseMapper.toDto(saved);
    }
}
