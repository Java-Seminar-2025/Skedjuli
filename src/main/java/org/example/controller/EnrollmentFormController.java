package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.business.EnrollmentPdfService;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollmentForms")
@RequiredArgsConstructor
public class EnrollmentFormController {
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final EnrollmentPdfService enrollmentPdfService;

    @PostMapping("/{formId}/approve")
    public void approveForm (@PathVariable Long formId){
        enrollmentFormDomainService.approveForm(formId);
    }

    @GetMapping("/{formId}/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @PathVariable Long formId,
            @RequestParam Long studentId
    ) {
        byte[] pdf = enrollmentPdfService.generatePdf(formId, studentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=upisni-list.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/{formId}/lock")
    public void lockForm(@PathVariable Long formId){
        enrollmentFormDomainService.lockForm(formId);
    }

}
