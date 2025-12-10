package org.example.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.dto.EnrollmentCourseResponse;
import org.example.domain.entity.EnrollmentFormItemEntity;
import org.example.repository.EnrollmentFormItemRepository;
import org.example.domain.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@AllArgsConstructor
@Service
public class EnrollmentFormItemDomainService {
    private final EnrollmentFormItemRepository enrollmentFormItemRepository;

    public List<EnrollmentCourseResponse> getEnrollmentFormItems(Long enrollmentFormId) {
        return enrollmentFormItemRepository.findByEnrollmentForm_Id(enrollmentFormId)
                .stream()
                .map(EnrollmentFormItemEntity::getCourse)
                .map(CourseMapper::toDto)
                .toList();
    }

    public int getTotalEctsForForm(Long enrollmentFormId) {
        return getEnrollmentFormItems(enrollmentFormId)
                .stream()
                .mapToInt(EnrollmentCourseResponse::ects)
                .sum();
    }
}
