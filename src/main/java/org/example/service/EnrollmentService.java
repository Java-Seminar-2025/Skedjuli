package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CompletedCourseRepository completedCourseRepository;
    private final EnrollmentFormRepository enrollmentFormRepository;
    private final EnrollmentFormItemRepository itemRepository;
    private final AcademicYearRepository academicYearRepository;

    private EnrollmentForm enrollSemester(Student student, Integer semester) {

        enrollmentFormRepository
                .findByStudentAndSemester(student, semester)
                .ifPresent(f -> {
                    throw new RuntimeException("Enrollment for semester " + semester + " already exists");
                });

        List<Course> mandatoryCourses =
                courseRepository.findByStudyProgramAndSemesterAndMandatoryTrue(
                        student.getStudyProgram(),
                        semester
                );

        List<Course> completedCourses = completedCourseRepository.findByStudent(student)
                .stream()
                .map(CompletedCourse::getCourse)
                .collect(Collectors.toList());

        EnrollmentForm form = new EnrollmentForm();
        form.setStudent(student);
        form.setSemester(semester);
        form.setAcademicYear(academicYearRepository.findByActiveTrue());
        form.setStatusEnum(EnrollmentForm.Status.PENDING);

        List<EnrollmentFormItem> items = new ArrayList<>();

        for (Course course : mandatoryCourses) {
            if (!completedCourses.contains(course)) {
                EnrollmentFormItem item = new EnrollmentFormItem();
                item.setEnrollmentForm(form);
                item.setCourse(course);
                item.setStatusEnum(EnrollmentFormItem.Status.PENDING);

                items.add(item);
            }
        }

        form.setItems(items);

        return enrollmentFormRepository.save(form);
    }
    public void enrollFirstYear(Student student) {
        Integer year = student.getCurrentYear();
        Integer sem1 = year * 2 - 1;  // 1
        Integer sem2 = year * 2;      // 2

        enrollSemester(student, sem1);
        enrollSemester(student, sem2);
    }

    public List<Course> getEnrolledCourses(Student student) {

        List<EnrollmentForm> forms = enrollmentFormRepository.findByStudent(student);

        return forms.stream()
                .flatMap(f -> f.getItems().stream().map(EnrollmentFormItem::getCourse))
                .collect(Collectors.toList());
    }
}