package org.example.service.business;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.AcademicYearCreateRequest;
import org.example.model.dto.AcademicYearDto;
import org.example.service.domain.AcademicYearDomainService;
import org.example.service.validator.AcademicYearValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActiveYearService {

    private final AcademicYearDomainService academicYearDomainService;
    private final AcademicYearValidator academicYearValidator;

    @Transactional
    public AcademicYearDto addAcademicYear(AcademicYearCreateRequest request) {
        academicYearValidator.validateCreate(request);
        return academicYearDomainService.createAcademicYear(request);
    }
}