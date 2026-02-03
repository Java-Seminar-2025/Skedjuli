package org.example.service.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CourseRequirementValidator {

    private CourseRequirementValidator() {}

    public static void validateNoCycle(
            Long courseId,
            Long requiredCourseId,
            Map<Long, List<Long>> adjacency
    ) {
        if (courseId.equals(requiredCourseId)) {
            throw new IllegalArgumentException("A course cannot require itself");
        }

        boolean createsCycle = isReachable(
                requiredCourseId,
                courseId,
                adjacency,
                new HashSet<>()
        );

        if (createsCycle) {
            throw new IllegalArgumentException(
                    "Adding prerequisite would create a cycle: "
                            + courseId + " <- ... <- " + requiredCourseId
            );
        }
    }

    private static boolean isReachable(
            Long current,
            Long target,
            Map<Long, List<Long>> adj,
            Set<Long> visited
    ) {
        return adj.getOrDefault(current, List.of()).stream().anyMatch(next ->
                target.equals(next) ||
                        (visited.add(next) && isReachable(next, target, adj, visited))
        );
    }
}
