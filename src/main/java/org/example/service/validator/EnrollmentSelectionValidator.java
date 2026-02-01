package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.CourseResponse;
import org.example.service.domain.CompletedCourseDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.CourseRequirementDomainService;
import org.example.service.domain.StudentDomainService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EnrollmentSelectionValidator {

    private final CourseRequirementDomainService courseRequirementDomainService;
    private final CompletedCourseDomainService completedCourseDomainService;
    private final StudentDomainService studentDomainService;
    private final CourseDomainService courseDomainService;

    public void validateEnrollment(Long studentId,
                                   List<Long> selectedCourseIds,
                                   boolean allowHigherYearSelection) {

        if (studentId == null) throw new IllegalArgumentException("studentId is required");
        var year = studentDomainService.getCurrentYearById(studentId);
        if (year < 1 || year > 3) throw new IllegalArgumentException("Unsupported study year: " + year);

        if (selectedCourseIds == null || selectedCourseIds.isEmpty()) return;

        var selectedResolved = selectedCourseIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(id -> {
                    try { return courseDomainService.getCourseById(id); }
                    catch (RuntimeException ex) { throw new IllegalArgumentException("Selected course not found: " + id); }
                })
                .collect(Collectors.toMap(CourseResponse::id, c -> c, (a,b)->a, LinkedHashMap::new));

        var selectedIds = new LinkedHashSet<>(selectedResolved.keySet());
        var completed = completedCourseDomainService.getCompletedCourseIdSet(studentId);

        var alreadyCompleted = selectedIds.stream().filter(completed::contains).toList();
        if (!alreadyCompleted.isEmpty())
            throw new IllegalArgumentException("Cannot select already completed courses: " + alreadyCompleted);

        var higherYearOffenders = selectedResolved.values().stream()
                .filter(c -> { int courseYear = (c.semester() + 1) / 2; return !allowHigherYearSelection && courseYear > year; })
                .map(CourseResponse::id)
                .toList();
        if (!higherYearOffenders.isEmpty())
            throw new IllegalArgumentException("Higher-year selection not allowed for courses: " + higherYearOffenders);

        Map<Long, List<Long>> directMap = courseRequirementDomainService.getDirectPrereqsMap(selectedIds);
        selectedIds.forEach(id -> directMap.putIfAbsent(id, Collections.emptyList()));

        selectedIds.forEach(courseId -> {
            var seed = directMap.computeIfAbsent(courseId, courseRequirementDomainService::getDirectPrerequisiteIds);
            LinkedHashSet<Long> discovered = seed.stream().collect(Collectors.toCollection(LinkedHashSet::new));

            Stream.iterate(discovered, frontier -> frontier.stream()
                            .flatMap(node -> {
                                directMap.computeIfAbsent(node, courseRequirementDomainService::getDirectPrerequisiteIds);
                                return directMap.getOrDefault(node, Collections.emptyList()).stream();
                            })
                            .filter(n -> !discovered.contains(n))
                            .collect(Collectors.toCollection(LinkedHashSet::new))
                    )
                    .takeWhile(f -> !f.isEmpty())
                    .forEach(f -> {
                        if (f.contains(courseId)) {
                            throw new IllegalArgumentException("Detected prerequisite cycle involving course " + courseId);
                        }
                        discovered.addAll(f);
                    });

            discovered.remove(courseId);

            var missing = discovered.stream()
                    .filter(pr -> !completed.contains(pr))
                    .filter(pr -> !selectedIds.contains(pr))
                    .toList();
            if (!missing.isEmpty()) {
                throw new IllegalArgumentException("Course " + courseId + " missing prerequisites: " + missing);
            }
        });
    }
}
