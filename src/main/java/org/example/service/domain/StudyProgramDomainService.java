package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.model.dto.StudyProgramDto;
import org.example.model.entity.StudyProgramEntity;
import org.example.model.mapper.StudyProgramMapper;
import org.example.repository.StudyProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyProgramDomainService {
    private final StudyProgramRepository studyProgramRepository;
    private final StudyProgramMapper studyProgramMapper;

    public boolean existsById(Long id) {
        return studyProgramRepository.existsById(id);
    }

    public List<StudyProgramDto> getAllStudyPrograms() {
        return studyProgramRepository.findAll().stream()
                .map(studyProgramMapper::toStudyProgramDto)
                .toList();
    }
}