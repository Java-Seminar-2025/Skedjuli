package org.example.repository;

import org.example.model.entity.LecturerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<LecturerEntity, Long> {
    Optional<LecturerEntity> findByUser_Id(Long userId);
}
