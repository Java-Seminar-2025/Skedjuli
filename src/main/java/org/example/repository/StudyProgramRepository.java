package org.example.repository;

import org.example.model.entity.StudyProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
public interface StudyProgramRepository extends JpaRepository<StudyProgramEntity, Long> {
    boolean existsByCode(String code);
=======
public interface StudyProgramRepository extends JpaRepository<StudyProgramEntity,Long> {
    Boolean existsByCode(String code);
>>>>>>> origin/enrollment
}
