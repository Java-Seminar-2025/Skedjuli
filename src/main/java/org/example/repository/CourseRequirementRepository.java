package org.example.repository;

import org.example.model.entity.CourseRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CourseRequirementRepository extends JpaRepository<CourseRequirementEntity, Long> {
    List<CourseRequirementEntity> findByCourse_Id(Long courseId);
    List<CourseRequirementEntity> findByCourse_IdIn(Collection<Long> courseIds);
}
