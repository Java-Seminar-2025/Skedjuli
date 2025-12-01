package org.example.repository;

import org.example.model.StudyProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyProgramRepository extends JpaRepository<StudyProgram,Long> {
    Optional<StudyProgram> findById(Long id);
}
