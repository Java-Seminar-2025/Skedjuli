package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.StudyProgramResponse;
import org.example.service.domain.CourseDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerStudyProgramService {
    private final CourseDomainService courseDomainService;

    public List<StudyProgramResponse> getStudyProgramsForLecturer(Long lecturerId) {
        return courseDomainService.getStudyProgramsForLecturer(lecturerId);
    }
}
