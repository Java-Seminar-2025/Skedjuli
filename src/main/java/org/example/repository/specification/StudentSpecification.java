package org.example.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<StudentEntity> filter(
            Long studyProgramId,
            Integer enrollmentYear,
            Integer currentYear,
            Double totalEctsEarned,
            Boolean isActive
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (studyProgramId != null) {
                predicates.add(cb.equal(root.get("studyProgram").get("id"), studyProgramId));
            }
            if (enrollmentYear != null) {
                predicates.add(cb.equal(root.get("enrollmentYear"), enrollmentYear));
            }
            if (currentYear != null) {
                predicates.add(cb.equal(root.get("currentYear"), currentYear));
            }
            if (totalEctsEarned != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalEctsEarned"), totalEctsEarned));
            }
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
