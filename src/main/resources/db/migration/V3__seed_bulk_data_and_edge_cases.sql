-- V3__seed_bulk_data.sql
-- Bulk seed for more realistic dataset

-- More study programs
INSERT INTO study_programs (code, name, description, duration_years, total_ects)
VALUES
('IT', 'Information Technology', 'BSc program', 3, 180),
('SE', 'Software Engineering', 'BSc program', 3, 180);

-- More academic years
INSERT INTO academic_years (year_code, start_date, end_date, enrollment_start, enrollment_end, is_active)
VALUES
('2024/2025', '2024-10-01', '2025-09-30', '2024-09-01', '2024-10-15', FALSE);

-- More users (professors + students)
INSERT INTO users (username, email, password_hash, first_name, last_name, role, date_of_birth)
VALUES
('prof2', 'prof2@example.com', '{noop}prof2', 'Marija', 'Nastavnik', 2, '1978-03-02'),
('stud2', 'stud2@example.com', '{noop}stud2', 'Luka', 'Horvat', 1, '2002-02-11'),
('stud3', 'stud3@example.com', '{noop}stud3', 'Ana', 'Kovac', 1, '2001-07-08');

INSERT INTO lecturers (user_id, department, academic_title, office_location, phone_number)
VALUES
((SELECT id FROM users WHERE username='prof2'), 'Software', 'MSc', 'C-101', '+385-1-555-0202');

INSERT INTO students (user_id, study_program_id, enrollment_year, current_year, average_grade, total_ects_earned)
VALUES
((SELECT id FROM users WHERE username='stud2'), (SELECT id FROM study_programs WHERE code='IT'), 2024, 2, 4.1, 60),
((SELECT id FROM users WHERE username='stud3'), (SELECT id FROM study_programs WHERE code='SE'), 2023, 3, 4.4, 120);

-- Extra courses (mix programs/semesters)
INSERT INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
VALUES
('IT101', 'Networks 1', 'Basics of networking', 6, TRUE, 150,
 (SELECT id FROM lecturers WHERE user_id = (SELECT id FROM users WHERE username='prof2')),
 (SELECT id FROM study_programs WHERE code='IT'),
 (SELECT id FROM academic_years WHERE year_code='2025/2026'),
 1, TRUE),

('SE301', 'Software Architecture', 'Designing systems', 6, TRUE, 100,
 (SELECT id FROM lecturers WHERE user_id = (SELECT id FROM users WHERE username='prof2')),
 (SELECT id FROM study_programs WHERE code='SE'),
 (SELECT id FROM academic_years WHERE year_code='2025/2026'),
 5, TRUE);
