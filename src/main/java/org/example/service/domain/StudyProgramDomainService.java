package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyProgramDomainService {
    private final StudyProgramRepository studyProgramRepository;

    public boolean existsById(Long id) {
        return studyProgramRepository.existsById(id);
    }
}
