package org.example.repository.specification;

import org.example.model.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<StudentEntity> isActive (Boolean active) {
        return (root, query, cb) -> active == null ? null : cb.equal(root.get("isActive"), active);
    }

    public static Specification<StudentEntity> hasStudyProgram (Long studyProgramId) {
        return (root,query, cb) -> studyProgramId == null ? null : cb.equal(root.get("studyProgram").get("id"), studyProgramId);
    }

    public static Specification<StudentEntity> hasEnrollmentYear (Integer enrollmentYear) {
        return (root, query, cb) -> enrollmentYear == null ? null :cb.equal(root.get("enrollmentYear"), enrollmentYear);
    }
}
