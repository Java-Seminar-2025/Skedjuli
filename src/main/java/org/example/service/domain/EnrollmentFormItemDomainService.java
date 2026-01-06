package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.model.dto.CourseInfo;
import org.example.model.entity.CourseEntity;
import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.example.model.enums.EnrollmentFormItemStatus;
import org.example.model.mapper.CourseMapper;
import org.example.repository.EnrollmentFormItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Domain service owning EnrollmentFormItemRepository only.
 * Exposes only DTOs/ids to callers; it never returns entity objects.
 */
@Service
@AllArgsConstructor
public class EnrollmentFormItemDomainService {

    private final EnrollmentFormItemRepository enrollmentFormItemRepository;
    private final CourseMapper courseMapper;

    /**
     * Return CourseInfo DTOs for items on the given enrollment form.
     */
    @Transactional(readOnly = true)
    public List<CourseInfo> getEnrollmentFormItems(Long enrollmentFormId) {
        return enrollmentFormItemRepository.findByEnrollmentForm_Id(enrollmentFormId)
                .stream()
                .map(EnrollmentFormItemEntity::getCourse)
                .map(courseMapper::toCourseInfo)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Delete all items on the given form that reference any of the supplied courseIds.
     * Only uses this repository.
     */
    @Transactional
    public void deleteByFormAndCourseIds(Long formId, Collection<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) return;

        var toDelete = enrollmentFormItemRepository.findByEnrollmentForm_Id(formId).stream()
                .filter(e -> e.getCourse() != null && courseIds.contains(e.getCourse().getId()))
                .toList();

        if (!toDelete.isEmpty()) enrollmentFormItemRepository.deleteAllInBatch(toDelete);
    }

    /**
     * Create and persist EnrollmentFormItemEntity entries for the given form and course ids.
     * Entities are constructed with minimal references (only ids) so that no extra reads are required.
     */
    @Transactional
    public void saveItemsForForm(Long formId, List<Long> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) return;

        var items = courseIds.stream()
                .filter(Objects::nonNull)
                .map(courseId -> {
                    var item = new EnrollmentFormItemEntity();

                    var formRef = new EnrollmentFormEntity();
                    formRef.setId(formId);
                    item.setEnrollmentForm(formRef);

                    var courseRef = new CourseEntity();
                    courseRef.setId(courseId);
                    item.setCourse(courseRef);

                    item.setStatusEnum(EnrollmentFormItemStatus.PENDING);
                    item.setCreatedAt(LocalDateTime.now());
                    return item;
                })
                .toList();

        if (!items.isEmpty()) enrollmentFormItemRepository.saveAll(items);
    }

    /**
     * Return total ects for a form by summing the ects of the attached course entities.
     * This method is read-only and returns a primitive int.
     */
    @Transactional(readOnly = true)
    public int getTotalEctsForForm(Long enrollmentFormId) {
        return enrollmentFormItemRepository.findByEnrollmentForm_Id(enrollmentFormId)
                .stream()
                .map(EnrollmentFormItemEntity::getCourse)
                .filter(Objects::nonNull)
                .mapToInt(CourseEntity::getEcts)
                .sum();
    }
}
