package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.StudentPatchRequest;
import org.example.service.domain.StudentDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentDomainService studentDomainService;

    @Transactional
    public void patchStudent(Long studentId, StudentPatchRequest request) {
        studentDomainService.patchStudent(studentId, request);
    }
}
