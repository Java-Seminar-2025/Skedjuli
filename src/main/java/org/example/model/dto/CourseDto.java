package org.example.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer ects;

    private Boolean mandatory;
    private Integer enrollmentLimit;
    private Integer semester;
    private Boolean active;

    private Long lecturerId;
    private Long studyProgramId;
    private Long academicYearId;
}
