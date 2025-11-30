package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer ects;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean mandatory = true;

    @Column(name = "enrollment_limit")
    private Integer enrollmentLimit;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToOne
    @JoinColumn(name = "study_program_id")
    private StudyProgram studyProgram;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    @Column(nullable = false)
    private Integer semester;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "course_req",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "req_course_id")
    )
    private Set<Course> prerequisites = new HashSet<>();

    @ManyToMany(mappedBy = "prerequisites")
    private Set<Course> dependentCourses = new HashSet<>();

    public Course() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getEcts() { return ects; }
    public void setEcts(Integer ects) { this.ects = ects; }

    public Boolean getMandatory() { return mandatory; }
    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }

    public Integer getEnrollmentLimit() { return enrollmentLimit; }
    public void setEnrollmentLimit(Integer enrollmentLimit) { this.enrollmentLimit = enrollmentLimit; }

    public Lecturer getLecturer() { return lecturer; }
    public void setLecturer(Lecturer lecturer) { this.lecturer = lecturer; }

    public StudyProgram getStudyProgram() { return studyProgram; }
    public void setStudyProgram(StudyProgram studyProgram) { this.studyProgram = studyProgram; }

    public AcademicYear getAcademicYear() { return academicYear; }
    public void setAcademicYear(AcademicYear academicYear) { this.academicYear = academicYear; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Set<Course> getPrerequisites() { return prerequisites; }
    public void setPrerequisites(Set<Course> prerequisites) { this.prerequisites = prerequisites; }

    public Set<Course> getDependentCourses() { return dependentCourses; }
    public void setDependentCourses(Set<Course> dependentCourses) { this.dependentCourses = dependentCourses; }
}
