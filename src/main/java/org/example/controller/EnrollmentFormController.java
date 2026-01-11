package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollmentForms")
@RequiredArgsConstructor
public class EnrollmentFormController {
    private final EnrollmentFormDomainService enrollmentFormDomainService;

    @PostMapping("/{formId}/approve")
    public void approveForm (@PathVariable Long formId){
        enrollmentFormDomainService.approveForm(formId);
    }
}
