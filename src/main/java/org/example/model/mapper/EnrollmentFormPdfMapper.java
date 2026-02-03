package org.example.model.mapper;

import org.example.model.dto.pdf.EnrollmentFormItemPdfDto;
import org.example.model.dto.pdf.EnrollmentFormPdfDto;
import org.example.model.entity.EnrollmentFormEntity;
import org.example.model.entity.EnrollmentFormItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EnrollmentFormPdfMapper {

    public EnrollmentFormPdfDto toPdfDto(EnrollmentFormEntity entity) {
        return new EnrollmentFormPdfDto(
                entity.getStudent().getUser().getFirstName() + " " + entity.getStudent().getUser().getLastName(),
                entity.getStudent().getStudyProgram().getName(),
                entity.getAcademicYear().getYearCode(),
                entity.getSemester(),
                entity.getStatusEnum().name(),
                entity.getSubmittedAt(),
                entity.getApprovedAt(),
                mapItems(entity.getItems())
        );
    }

    private List<EnrollmentFormItemPdfDto> mapItems(List<EnrollmentFormItemEntity> items) {
        return items.stream()
                .map(item -> new EnrollmentFormItemPdfDto(
                        item.getCourse().getCode(),
                        item.getCourse().getName(),
                        item.getCourse().getEcts(),
                        item.getCourse().getSemester(),
                        item.getStatusEnum().name()
                ))
                .toList();
    }
}
