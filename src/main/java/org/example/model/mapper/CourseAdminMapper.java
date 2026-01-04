package org.example.model.mapper;

import org.example.model.dto.CourseAdminReadDto;
import org.example.model.dto.CourseCreateRequest;
import org.example.model.dto.CourseUpdateRequest;
import org.example.model.dto.IdNameDto;
import org.example.model.entity.CourseEntity;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class CourseAdminMapper {

    public  CourseAdminReadDto toAdminReadDto(CourseEntity entity) {
        if (entity == null) return null;

        return new CourseAdminReadDto(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getEcts(),
                entity.getSemester(),
                entity.getMandatory(),
                entity.getEnrollmentLimit(),
                entity.getActive(),
                entity.getStudyProgram() == null
                        ? null
                        : new IdNameDto(
                        entity.getStudyProgram().getId(),
                        entity.getStudyProgram().getName()
                ),
                entity.getAcademicYear() == null
                        ? null
                        : new IdNameDto(
                        entity.getAcademicYear().getId(),
                        entity.getAcademicYear().getYearCode()
                ),
                entity.getLecturer() == null
                        ? null
                        : new IdNameDto(
                        entity.getLecturer().getId(),
                        entity.getLecturer().getFullName()
                ),
                entity.getPrerequisites().stream()
                        .map(c -> new IdNameDto(c.getId(), c.getName()))
                        .toList()
        );
    }

    public CourseEntity fromCreateDto(CourseCreateRequest dto) {
        if (dto == null) return null;

        CourseEntity entity = new CourseEntity();
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setEcts(dto.ects());
        entity.setSemester(dto.semester());
        entity.setMandatory(dto.mandatory());
        entity.setEnrollmentLimit(dto.enrollmentLimit());
        entity.setActive(true);
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public void updateEntity(
            CourseEntity entity,
            CourseUpdateRequest dto
    ) {
        if (entity == null || dto == null) return;

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setEcts(dto.ects());
        entity.setSemester(dto.semester());
        entity.setMandatory(dto.mandatory());
        entity.setEnrollmentLimit(dto.enrollmentLimit());
        entity.setActive(dto.active());
        entity.setUpdatedAt(LocalDateTime.now());
    }

}
