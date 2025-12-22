package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseRequirementDto;
import org.example.exception.EnrollmentValidationException;
import org.example.service.domain.CourseRequirementDomainService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CourseRequirementValidator {

    private final CourseRequirementDomainService courseRequirementDomainService;

    /* ------------------- Public void API ------------------- */

    public void validateIntegrity(CourseRequirementDto dto) {
        if (dto == null) throw new EnrollmentValidationException("CourseRequirement is null");
        if (dto.courseId() == null)
            throw new EnrollmentValidationException("courseId is null for requirement id " + dto.id());
        if (dto.requiredCourseId() == null)
            throw new EnrollmentValidationException("requiredCourseId is null for requirement id " + dto.id());
        if (Objects.equals(dto.courseId(), dto.requiredCourseId()))
            throw new EnrollmentValidationException("A course cannot require itself: courseId=" + dto.courseId());
    }

    /**
     * Validate the entire collection: integrity + cycle detection using Kahn's method implemented recursively.
     * No for/while loops are used.
     */
    public void validateAll(Collection<CourseRequirementDto> dtos) {
        if (dtos == null || dtos.isEmpty()) return;

        // integrity checks
        dtos.forEach(this::validateIntegrity);

        // build adjacency: courseId -> set(requiredCourseId)
        Map<Long, Set<Long>> adj = dtos.stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementDto::courseId,
                        Collectors.mapping(CourseRequirementDto::requiredCourseId, Collectors.toCollection(LinkedHashSet::new))
                ));

        // all nodes = courses and required courses
        LinkedHashSet<Long> allNodes = dtos.stream()
                .flatMap(d -> Stream.of(d.courseId(), d.requiredCourseId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // ensure all nodes present as keys
        allNodes.forEach(id -> adj.putIfAbsent(id, Collections.emptySet()));

        // indegree map
        Map<Long, Integer> indegree = allNodes.stream()
                .collect(Collectors.toMap(n -> n, n -> 0, (a, b) -> a, HashMap::new));
        adj.values().stream().flatMap(Set::stream).forEach(v -> indegree.merge(v, 1, Integer::sum));

        // start recursive Kahn processing
        Set<Long> processed = new LinkedHashSet<>();
        processKahnRecursive(adj, indegree, processed);

        // if not all nodes processed -> cycle exists; compute cycle nodes
        if (processed.size() != allNodes.size()) {
            List<Long> cycleNodes = indegree.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .map(Entry::getKey)
                    .toList();
            throw new EnrollmentValidationException("Prerequisite cycle detected among course IDs: " + cycleNodes);
        }
    }

    /**
     * Validate that inserting edge (courseId <- requiredCourseId) will NOT create a cycle.
     * Builds a full adjacency map (single fetch) and checks reachability using recursion + streams.
     * No loops used.
     */
    public void validateNoCycleOnCreate(Long courseId, Long requiredCourseId) {
        if (courseId == null || requiredCourseId == null)
            throw new EnrollmentValidationException("Course IDs must be non-null");
        if (Objects.equals(courseId, requiredCourseId))
            throw new EnrollmentValidationException("A course cannot require itself: " + courseId);

        // full adjacency map (single fetch via domain service)
        Map<Long, List<Long>> adjList = courseRequirementDomainService.findAll().stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementDto::courseId,
                        Collectors.mapping(CourseRequirementDto::requiredCourseId, Collectors.toList())
                ));

        // reachability check via recursion + streams
        boolean reachable = isReachable(requiredCourseId, courseId, adjList, new HashSet<>());
        if (reachable) {
            throw new EnrollmentValidationException("Adding requirement would create cycle: "
                    + courseId + " <- ... <- " + requiredCourseId);
        }
    }

    /* ------------------- Helpers (recursive & streamy) ------------------- */

    /**
     * Recursive implementation of Kahn's algorithm:
     * - finds zero-indegree nodes not yet processed,
     * - subtracts their outgoing edges from indegree to produce a new indegree map,
     * - recurses until no zero nodes remain.
     * No explicit loops used here (recursion and streams only).
     */
    private void processKahnRecursive(Map<Long, Set<Long>> adj,
                                      Map<Long, Integer> indegree,
                                      Set<Long> processed) {
        // find zero-indegree nodes not yet processed
        Set<Long> zero = indegree.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .map(Entry::getKey)
                .filter(n -> !processed.contains(n))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // base: no new zero nodes
        if (zero.isEmpty()) return;

        // mark them processed (new set)
        Set<Long> newProcessed = new LinkedHashSet<>(processed);
        newProcessed.addAll(zero);

        // compute new indegree map by subtracting edges from zero nodes
        Map<Long, Integer> newIndegree = indegree.keySet().stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> {
                            // if this node is already processed in newProcessed, mark as -1 to exclude it
                            if (newProcessed.contains(k)) return -1;
                            // original degree
                            int orig = indegree.getOrDefault(k, 0);
                            // number of edges from zero nodes to k
                            long removed = zero.stream()
                                    .filter(z -> adj.getOrDefault(z, Collections.emptySet()).contains(k))
                                    .count();
                            int val = (int) (orig - removed);
                            return Math.max(val, 0);
                        },
                        (a, b) -> a,
                        HashMap::new
                ));

        // recurse with updated state
        processKahnRecursive(adj, newIndegree, newProcessed);

        // propagate newProcessed into processed (side effect on original set)
        processed.clear();
        processed.addAll(newProcessed);
    }

    /**
     * Recursive reachability check using streams.
     * Returns true if target is reachable from current.
     * visited prevents infinite recursion.
     */
    private boolean isReachable(Long current,
                                Long target,
                                Map<Long, List<Long>> adjList,
                                Set<Long> visited) {
        var neighbors = adjList.getOrDefault(current, Collections.emptyList());
        return neighbors.stream().anyMatch(n ->
                Objects.equals(n, target) ||
                        (visited.add(n) && isReachable(n, target, adjList, visited))
        );
    }
}
