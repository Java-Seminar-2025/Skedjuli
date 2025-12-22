package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.CourseRequirementDto;
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
        if (dto == null) throw new IllegalArgumentException("CourseRequirement is null");
        if (dto.courseId() == null)
            throw new IllegalArgumentException("courseId is null for requirement id " + dto.id());
        if (dto.requiredCourseId() == null)
            throw new IllegalArgumentException("requiredCourseId is null for requirement id " + dto.id());
        if (Objects.equals(dto.courseId(), dto.requiredCourseId()))
            throw new IllegalArgumentException("A course cannot require itself: courseId=" + dto.courseId());
    }

    /**
     * Validate the entire collection: integrity + cycle detection using Kahn's method implemented recursively.
     * No for/while loops are used.
     */
    public void validateAll(Collection<CourseRequirementDto> dtos) {
        if (dtos == null || dtos.isEmpty()) return;

        dtos.forEach(this::validateIntegrity);

        Map<Long, Set<Long>> adj = dtos.stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementDto::courseId,
                        Collectors.mapping(CourseRequirementDto::requiredCourseId, Collectors.toCollection(LinkedHashSet::new))
                ));

        LinkedHashSet<Long> allNodes = dtos.stream()
                .flatMap(d -> Stream.of(d.courseId(), d.requiredCourseId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        allNodes.forEach(id -> adj.putIfAbsent(id, Collections.emptySet()));

        Map<Long, Integer> indegree = allNodes.stream()
                .collect(Collectors.toMap(n -> n, n -> 0, (a, b) -> a, HashMap::new));
        adj.values().stream().flatMap(Set::stream).forEach(v -> indegree.merge(v, 1, Integer::sum));

        Set<Long> processed = new LinkedHashSet<>();
        processKahnRecursive(adj, indegree, processed);

        if (processed.size() != allNodes.size()) {
            List<Long> cycleNodes = indegree.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .map(Entry::getKey)
                    .toList();
            throw new IllegalArgumentException("Prerequisite cycle detected among course IDs: " + cycleNodes);
        }
    }

    /**
     * Validate that inserting edge (courseId <- requiredCourseId) will NOT create a cycle.
     * Builds a full adjacency map (single fetch) and checks reachability using recursion + streams.
     * No loops used.
     */
    public void validateNoCycleOnCreate(Long courseId, Long requiredCourseId) {
        if (courseId == null || requiredCourseId == null)
            throw new IllegalArgumentException("Course IDs must be non-null");
        if (Objects.equals(courseId, requiredCourseId))
            throw new IllegalArgumentException("A course cannot require itself: " + courseId);

        Map<Long, List<Long>> adjList = courseRequirementDomainService.findAll().stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementDto::courseId,
                        Collectors.mapping(CourseRequirementDto::requiredCourseId, Collectors.toList())
                ));

        boolean reachable = isReachable(requiredCourseId, courseId, adjList, new HashSet<>());
        if (reachable) {
            throw new IllegalArgumentException("Adding requirement would create cycle: "
                    + courseId + " <- ... <- " + requiredCourseId);
        }
    }

    /* ------------------- Helpers (recursive & streamy) ------------------- */

    private void processKahnRecursive(Map<Long, Set<Long>> adj,
                                      Map<Long, Integer> indegree,
                                      Set<Long> processed) {
        Set<Long> zero = indegree.entrySet().stream()
                .filter(e -> e.getValue() == 0)
                .map(Entry::getKey)
                .filter(n -> !processed.contains(n))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (zero.isEmpty()) return;

        Set<Long> newProcessed = new LinkedHashSet<>(processed);
        newProcessed.addAll(zero);

        Map<Long, Integer> newIndegree = indegree.keySet().stream()
                .collect(Collectors.toMap(
                        k -> k,
                        k -> {
                            if (newProcessed.contains(k)) return -1;
                            int orig = indegree.getOrDefault(k, 0);
                            long removed = zero.stream()
                                    .filter(z -> adj.getOrDefault(z, Collections.emptySet()).contains(k))
                                    .count();
                            int val = (int) (orig - removed);
                            return Math.max(val, 0);
                        },
                        (a, b) -> a,
                        HashMap::new
                ));

        processKahnRecursive(adj, newIndegree, newProcessed);

        processed.clear();
        processed.addAll(newProcessed);
    }

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
