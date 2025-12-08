package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.entity.StudyProgramEntity;
import org.example.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyProgramDomainService {
    private final StudyProgramRepository studyProgramRepository;

    public StudyProgramEntity getById(Long id) {
        return studyProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Study program not found"));
    }
}