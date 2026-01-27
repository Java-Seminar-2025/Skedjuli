package org.example.service.business;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.request.create.CourseRequirementCreateRequest;
import org.example.model.dto.request.patch.CoursePatchRequest;
import org.example.model.dto.request.patch.CourseRequirementPatchRequest;
import org.example.model.dto.response.CourseRequirementResponse;
import org.example.model.dto.response.CourseResponse;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.CourseRequirementDomainService;
import org.example.service.validator.CourseRequirementValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseManagementService {

    private final CourseDomainService courseDomainService;
    private final CourseRequirementDomainService courseRequirementDomainService;

    @Transactional
    public CourseResponse createCourseWithRequirements(CourseCreateRequest request) {

        var course = courseDomainService.createCourse(request);

        Optional.ofNullable(request.prerequisiteCourseIds())
                .stream()
                .flatMap(Collection::stream)
                .peek(prereqId ->
                        CourseRequirementValidator.validateNoCycle(
                                course.id(),
                                prereqId,
                                loadAdjacency()
                        )
                )
                .map(prereqId ->
                        new CourseRequirementCreateRequest(course.id(), prereqId)
                )
                .forEach(courseRequirementDomainService::createCourseRequirement);

        return course;
    }



    public CourseResponse getCourseWithRequirements(Long id) {
        return courseDomainService.getCourseById(id);
    }

    @Transactional
    public CourseResponse patchCourseWithRequirements(Long courseId, CoursePatchRequest request) {

        var updatedCourse = courseDomainService.patchCourse(courseId, request);

        Optional.ofNullable(request.prerequisiteCourseIds())
                .ifPresent(prereqs -> {

                    courseRequirementDomainService.deleteRequirementsForCourse(courseId);

                    Map<Long, List<Long>> adjacency = loadAdjacency();

                    prereqs.stream()
                            .peek(prereqId ->
                                    CourseRequirementValidator.validateNoCycle(
                                            courseId,
                                            prereqId,
                                            adjacency
                                    )
                            )
                            .peek(prereqId ->
                                    adjacency
                                            .computeIfAbsent(courseId, k -> new ArrayList<>())
                                            .add(prereqId)
                            )
                            .map(prereqId ->
                                    new CourseRequirementCreateRequest(courseId, prereqId)
                            )
                            .forEach(courseRequirementDomainService::createCourseRequirement);
                });

        return updatedCourse;
    }


    @Transactional
    public void deleteCourseWithRequirements(Long courseId) {
        courseRequirementDomainService.deleteRequirementsForCourse(courseId);
        courseDomainService.deleteCourse(courseId);
    }

    private Map<Long, List<Long>> loadAdjacency() {
        return courseRequirementDomainService.findAll().stream()
                .collect(Collectors.groupingBy(
                        CourseRequirementResponse::courseId,
                        Collectors.mapping(
                                CourseRequirementResponse::requiredCourseId,
                                Collectors.toList()
                        )
                ));
    }

    @Transactional
    public CourseRequirementResponse createCourseRequirement(CourseRequirementCreateRequest request) {
        return courseRequirementDomainService.createCourseRequirement(request);
    }

    public List<CourseRequirementResponse> getCourseRequirements(Long courseId) {
        return courseRequirementDomainService.findByCourseId(courseId);
    }

    @Transactional
    public CourseRequirementResponse patchCourseRequirement(Long id, CourseRequirementPatchRequest request) {
        return courseRequirementDomainService.patchCourseRequirement(id, request);
    }

    @Transactional
    public void deleteCourseRequirement(Long id) {
        courseRequirementDomainService.deleteCourseRequirement(id);
    }

    @Transactional
    public void deleteRequirementsForCourse(Long courseId) {
        courseRequirementDomainService.deleteRequirementsForCourse(courseId);
    }
}
