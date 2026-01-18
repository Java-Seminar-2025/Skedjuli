INSERT IGNORE INTO study_programs (code, name, description, duration_years, total_ects, is_active)
VALUES ('CS', 'Computer Science', 'Computer Science program', 3, 180, TRUE);

INSERT IGNORE INTO academic_years (year_code, start_date, end_date, is_active)
VALUES ('2025/2026', '2025-10-01', '2026-09-30', TRUE);

INSERT IGNORE INTO users (username, email, password_hash, first_name, last_name, role)
VALUES ('jaja', 'jaja', '$2a$10$KgR/7EKAXOTNrgJ8WWu65uYO2KOEJetpeYqAxb1WVl1Ld10I0bz7u', 'Jaja', 'Lecturer', 2);

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Computer Science', 'Assistant', 'A-101', '000-000', TRUE
FROM users u
WHERE u.email='jaja';

INSERT IGNORE INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    'CS101', 'Programming Fundamentals', 'Intro to programming in Java', 6, TRUE, 120,
    l.id, sp.id, ay.id, 1, TRUE
FROM lecturers l
         JOIN users u ON u.id=l.user_id
         JOIN study_programs sp ON sp.code='CS'
         JOIN academic_years ay ON ay.year_code='2025/2026'
WHERE u.email='jaja';

INSERT IGNORE INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    'CS102', 'Discrete Mathematics', 'Logic, sets, relations, graphs', 6, TRUE, 120,
    l.id, sp.id, ay.id, 1, TRUE
FROM lecturers l
         JOIN users u ON u.id=l.user_id
         JOIN study_programs sp ON sp.code='CS'
         JOIN academic_years ay ON ay.year_code='2025/2026'
WHERE u.email='jaja';

INSERT IGNORE INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    'CS103', 'Computer Systems', 'CPU, memory, OS basics', 6, TRUE, 120,
    l.id, sp.id, ay.id, 1, TRUE
FROM lecturers l
         JOIN users u ON u.id=l.user_id
         JOIN study_programs sp ON sp.code='CS'
         JOIN academic_years ay ON ay.year_code='2025/2026'
WHERE u.email='jaja';

INSERT IGNORE INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    'CS104', 'Web Foundations', 'HTTP, REST, basics of web dev', 6, TRUE, 120,
    l.id, sp.id, ay.id, 1, TRUE
FROM lecturers l
         JOIN users u ON u.id=l.user_id
         JOIN study_programs sp ON sp.code='CS'
         JOIN academic_years ay ON ay.year_code='2025/2026'
WHERE u.email='jaja';

INSERT IGNORE INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    'CS105', 'Algorithms & Data Structures', 'Complexity, lists, trees, sorting', 5, TRUE, 120,
    l.id, sp.id, ay.id, 1, TRUE
FROM lecturers l
         JOIN users u ON u.id=l.user_id
         JOIN study_programs sp ON sp.code='CS'
         JOIN academic_years ay ON ay.year_code='2025/2026'
WHERE u.email='jaja';