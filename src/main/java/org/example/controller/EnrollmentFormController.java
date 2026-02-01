package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.response.EnrollmentFormLockedResponse;
import org.example.service.business.EnrollmentPdfService;
import org.example.service.business.LecturerEnrollmentFormService;
import org.example.service.domain.EnrollmentFormDomainService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollmentForms")
@RequiredArgsConstructor
public class EnrollmentFormController {
    private final EnrollmentFormDomainService enrollmentFormDomainService;
    private final EnrollmentPdfService enrollmentPdfService;
    private final LecturerEnrollmentFormService lecturerEnrollmentFormService;

    @PostMapping("/{formId}/approve")
    public void approveForm (@PathVariable Long formId, @RequestParam Long approverUserId) {
        enrollmentFormDomainService.approveForm(formId, approverUserId);
    }

    @PostMapping("/lecturer/approve")
    public ResponseEntity<Void> approveFormAsLecturer (@RequestParam Long formId, @RequestParam Long approverUserId) {
        lecturerEnrollmentFormService.approveFormAsLecturer(approverUserId, formId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lecturer/reject")
    public ResponseEntity<Void> rejectFormAsLecturer (@RequestParam Long formId, @RequestParam Long approverUserId) {
        lecturerEnrollmentFormService.rejectFormAsLecturer(approverUserId, formId);
        return ResponseEntity.noContent().build();
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

    @GetMapping("lecturer/locked")
    public List<EnrollmentFormLockedResponse> getLockedForms(@RequestParam Long lecturerId) {
        return lecturerEnrollmentFormService.getLockedForms(lecturerId);
    }

}
