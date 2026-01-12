-- V2__seed_full_data.sql
-- Seed base/reference data + a complete minimal graph

-- USERS
INSERT INTO users (username, email, password_hash, first_name, last_name, role, date_of_birth)
VALUES
('admin', 'admin@example.com', '{noop}admin', 'Admin', 'User', 3, '1990-01-01'),
('prof1', 'prof1@example.com', '{noop}prof1', 'Petar', 'Profesor', 2, '1980-05-10'),
('stud1', 'stud1@example.com', '{noop}stud1', 'Iva', 'Student', 1, '2003-09-20');

-- STUDY PROGRAM
INSERT INTO study_programs (code, name, description, duration_years, total_ects)
VALUES
('CS', 'Computer Science', 'BSc program', 3, 180);

-- ACADEMIC YEAR
INSERT INTO academic_years (year_code, start_date, end_date, enrollment_start, enrollment_end, is_active)
VALUES
('2025/2026', '2025-10-01', '2026-09-30', '2025-09-01', '2025-10-15', TRUE);

-- LECTURER
INSERT INTO lecturers (user_id, department, academic_title, office_location, phone_number)
VALUES
((SELECT id FROM users WHERE username='prof1'), 'Informatics', 'PhD', 'B-210', '+385-1-555-0101');

-- STUDENT
INSERT INTO students (user_id, study_program_id, enrollment_year, current_year, average_grade, total_ects_earned)
VALUES
((SELECT id FROM users WHERE username='stud1'),
 (SELECT id FROM study_programs WHERE code='CS'),
 2025, 1, NULL, 0);

-- COURSES (3 courses, year1/sem1 and sem2)
INSERT INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
VALUES
('CS101', 'Intro to CS', 'Basics', 6, TRUE, 200,
 (SELECT id FROM lecturers WHERE user_id = (SELECT id FROM users WHERE username='prof1')),
 (SELECT id FROM study_programs WHERE code='CS'),
 (SELECT id FROM academic_years WHERE year_code='2025/2026'),
 1, TRUE),

('CS102', 'Programming 1', 'Programming fundamentals', 6, TRUE, 200,
 (SELECT id FROM lecturers WHERE user_id = (SELECT id FROM users WHERE username='prof1')),
 (SELECT id FROM study_programs WHERE code='CS'),
 (SELECT id FROM academic_years WHERE year_code='2025/2026'),
 1, TRUE),

('CS201', 'Data Structures', 'Lists, trees, graphs', 6, TRUE, 150,
 (SELECT id FROM lecturers WHERE user_id = (SELECT id FROM users WHERE username='prof1')),
 (SELECT id FROM study_programs WHERE code='CS'),
 (SELECT id FROM academic_years WHERE year_code='2025/2026'),
 2, TRUE);

-- COURSE PREREQUISITES (CS201 requires CS102)
INSERT INTO course_req (course_id, req_course_id)
VALUES (
    (SELECT id FROM courses WHERE code='CS201'),
    (SELECT id FROM courses WHERE code='CS102')
);

-- COMPLETED COURSE (student completed CS101)
INSERT INTO completed_courses (student_id, course_id, academic_year_id, grade, completion_date)
VALUES (
    (SELECT id FROM students WHERE user_id = (SELECT id FROM users WHERE username='stud1')),
    (SELECT id FROM courses WHERE code='CS101'),
    (SELECT id FROM academic_years WHERE year_code='2025/2026'),
    5,
    '2025-12-20'
);
