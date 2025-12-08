package org.example.repository;

import org.example.domain.entity.StudyProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProgramRepository extends JpaRepository<StudyProgramEntity,Long> {}
