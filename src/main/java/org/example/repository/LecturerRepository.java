package org.example.repository;

import org.example.model.entity.LecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<LecturerEntity, Long> {}
