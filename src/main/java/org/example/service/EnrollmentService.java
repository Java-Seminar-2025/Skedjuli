package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.Course;
import org.example.model.EnrollmentForm;
import org.example.model.EnrollmentFormItem;
import org.example.model.Student;
import org.example.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CompletedCourseRepository completedCourseRepository;
    private final EnrollmentFormRepository enrollmentFormRepository;
    private final EnrollmentFormItemRepository itemRepository;

    private EnrollmentForm enrollSemester(Student student, Integer semester) {

        // prevent double enrollment for same semester
        enrollmentFormRepository
                .findByStudentIdAndSemester(student.getId(), semester)
                .ifPresent(f -> {
                    throw new RuntimeException("Enrollment for semester " + semester + " already exists");
                });

        // fetch mandatory courses
        List<Course> mandatoryCourses =
                courseRepository.findByStudyProgramAndSemesterAndMandatoryTrue(
                        student.getStudyProgram(),
                        semester
                );

        // create the form
        EnrollmentForm form = new EnrollmentForm();
        form.setStudent(student);
        form.setSemester(semester);
        form.setStatusEnum(EnrollmentForm.Status.PENDING);
        enrollmentFormRepository.save(form);

        // add only courses student has NOT completed
        List<Long> completedCourseIds = completedCourseRepository
                .findCourseIdsByStudentId(student.getId());

        for (Course course : mandatoryCourses) {
            if (!completedCourseIds.contains(course.getId())) {
                EnrollmentFormItem item = new EnrollmentFormItem();
                item.setEnrollmentForm(form);
                item.setCourse(course);
                item.setStatusEnum(EnrollmentFormItem.Status.PENDING);
                itemRepository.save(item);
            }
        }

        return form;
    }

    public List<EnrollmentForm> enrollFirstYear(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Integer year = student.getCurrentYear();
        Integer sem1 = year * 2 - 1;
        Integer sem2 = year * 2;

        return List.of(
                enrollSemester(student, sem1),
                enrollSemester(student, sem2)
        );
    }
}