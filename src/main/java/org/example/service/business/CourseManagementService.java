package org.example.service.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.CourseCreateRequest;
import org.example.model.dto.response.CourseResponse;
import org.example.service.domain.CourseDomainService;
import org.example.service.domain.CourseRequirementDomainService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseManagementService {

    private final CourseDomainService courseDomainService;
    private final CourseRequirementDomainService courseRequirementDomainService;

    @Transactional
    public CourseResponse createCourseWithRequirements(CourseCreateRequest request) {
        var courseResponse = courseDomainService.createCourse(request);

        if (request.prerequisiteCourseIds() != null && !request.prerequisiteCourseIds().isEmpty()) {
            var course = courseDomainService.getEntityReference(courseResponse.id());
            var courseMap = request.prerequisiteCourseIds().stream()
                    .collect(Collectors.toMap(id -> id, id -> courseDomainService.getEntityReference(id)));

            for (var reqId : request.prerequisiteCourseIds()) {
                courseRequirementDomainService.createRequirement(course, courseMap.get(reqId));
            }
        }

        return courseResponse;
    }
}