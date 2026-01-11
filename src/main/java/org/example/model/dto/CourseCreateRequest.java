package org.example.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {

    @NotBlank
    @Size(max = 20)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    @Min(1)
    private Integer ects;

    private Boolean mandatory;

    @Min(1)
    private Integer enrollmentLimit;

    @NotNull
    private Long studyProgramId;

    @NotNull
    private Long academicYearId;

    @NotNull
    @Min(1)
    private Integer semester;
}
