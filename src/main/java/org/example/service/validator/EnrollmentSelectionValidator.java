package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseInfo;
import org.example.exception.EnrollmentValidationException;
import org.example.service.domain.CompletedCourseDomainService;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.CourseRequirementDomainService;
import org.example.service.domain.StudentDomainService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Validator for enrollment selections: void methods, streams-only control flow,
 * no recursion and no explicit for/while loops.
 */
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

        if (studentId == null) throw new EnrollmentValidationException("studentId is required");
        var year = studentDomainService.getCurrentYearById(studentId);
        if (year < 1 || year > 3) throw new EnrollmentValidationException("Unsupported study year: " + year);

        if (selectedCourseIds == null || selectedCourseIds.isEmpty()) return;

        var selectedResolved = selectedCourseIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(id -> {
                    try { return courseDomainService.getCourseInfoById(id); }
                    catch (RuntimeException ex) { throw new EnrollmentValidationException("Selected course not found: " + id); }
                })
                .collect(Collectors.toMap(CourseInfo::id, c -> c, (a,b)->a, LinkedHashMap::new));

        var selectedIds = new LinkedHashSet<>(selectedResolved.keySet());
        var completed = completedCourseDomainService.getCompletedCourseIdSet(studentId);

        var alreadyCompleted = selectedIds.stream().filter(completed::contains).toList();
        if (!alreadyCompleted.isEmpty())
            throw new EnrollmentValidationException("Cannot select already completed courses: " + alreadyCompleted);

        var higherYearOffenders = selectedResolved.values().stream()
                .filter(c -> { int courseYear = (c.semester() + 1) / 2; return !allowHigherYearSelection && courseYear > year; })
                .map(CourseInfo::id)
                .toList();
        if (!higherYearOffenders.isEmpty())
            throw new EnrollmentValidationException("Higher-year selection not allowed for courses: " + higherYearOffenders);

        // preload direct prereqs for selected ids
        Map<Long, List<Long>> directMap = courseRequirementDomainService.getDirectPrereqsMap(selectedIds);
        selectedIds.forEach(id -> directMap.putIfAbsent(id, Collections.emptyList()));

        // For each selected course, expand transitive prerequisites using stream-based iterative expansion
        selectedIds.forEach(courseId -> {
            var seed = directMap.computeIfAbsent(courseId, courseRequirementDomainService::getDirectPrerequisiteIds);
            LinkedHashSet<Long> discovered = seed.stream().collect(Collectors.toCollection(LinkedHashSet::new));

            // iterative expansion with Stream.iterate (no loops/recursion)
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
                            throw new EnrollmentValidationException("Detected prerequisite cycle involving course " + courseId);
                        }
                        discovered.addAll(f);
                    });

            discovered.remove(courseId);

            var missing = discovered.stream()
                    .filter(pr -> !completed.contains(pr))
                    .filter(pr -> !selectedIds.contains(pr))
                    .toList();
            if (!missing.isEmpty()) {
                throw new EnrollmentValidationException("Course " + courseId + " missing prerequisites: " + missing);
            }
        });
    }
}
