package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudyProgramDto;
import org.example.service.domain.StudyProgramDomainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/studyPrograms")
@RequiredArgsConstructor
public class StudyProgramController {
    private final StudyProgramDomainService studyProgramDomainService;

    @GetMapping("/allStudyPrograms")
    public List<StudyProgramDto> getAllStudyPrograms() {
        return studyProgramDomainService.getAllStudyPrograms();
    }
}
