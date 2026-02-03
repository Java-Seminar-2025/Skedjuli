USE skedjuli;

-- ---------- USERS (LECTURERS + STUDENTS) ----------
-- roles: STUDENT=1, PROFESSOR=2, ADMIN=3

INSERT IGNORE INTO users (username, email, password_hash, first_name, last_name, role, date_of_birth)
VALUES
  -- EE lecturers
  ('eeprof1', 'eeprof1@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Dario', 'Kralj', 2, '1981-02-13'),
  ('eeprof2', 'eeprof2@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Irena', 'Zoric', 2, '1979-09-09'),

  -- ME lecturers
  ('meprof1', 'meprof1@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Tomislav', 'Grgic', 2, '1983-04-21'),
  ('meprof2', 'meprof2@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Marina', 'Vidic', 2, '1986-12-12'),

  -- BA lecturers
  ('baprof1', 'baprof1@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Nikola', 'Savic', 2, '1977-06-06'),
  ('baprof2', 'baprof2@example.com', '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Lea', 'Juric', 2, '1984-08-18'),

  -- EE students
  ('eestud1', 'eestud1@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Karlo', 'Simic', 1, '2003-01-05'),
  ('eestud2', 'eestud2@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Tea', 'Saric', 1, '2003-10-10'),
  ('eestud3', 'eestud3@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Nina', 'Lukic', 1, '2002-03-14'),

  -- ME students
  ('mestud1', 'mestud1@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Matej', 'Puljic', 1, '2003-07-07'),
  ('mestud2', 'mestud2@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Tara', 'Prlic', 1, '2004-02-02'),
  ('mestud3', 'mestud3@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Andrej', 'Bilik', 1, '2002-11-11'),

  -- BA students
  ('bastud1', 'bastud1@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Petra', 'Matic', 1, '2004-05-20'),
  ('bastud2', 'bastud2@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Lana', 'Knezic', 1, '2003-09-23'),
  ('bastud3', 'bastud3@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Josip', 'Radic', 1, '2002-01-30');

-- ---------- LECTURERS ----------
INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Electrical Engineering', 'Assistant', 'E-101', '410-101', TRUE
FROM users u WHERE u.email='eeprof1@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Electrical Engineering', 'Professor', 'E-102', '410-102', TRUE
FROM users u WHERE u.email='eeprof2@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Mechanical Engineering', 'Assistant', 'M-201', '420-201', TRUE
FROM users u WHERE u.email='meprof1@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Mechanical Engineering', 'Professor', 'M-202', '420-202', TRUE
FROM users u WHERE u.email='meprof2@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Business Administration', 'Assistant', 'B-301', '430-301', TRUE
FROM users u WHERE u.email='baprof1@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Business Administration', 'Professor', 'B-302', '430-302', TRUE
FROM users u WHERE u.email='baprof2@example.com';

-- ---------- STUDENTS ----------
-- Enrollment mix: year 1 + year 2 + year 3 to exercise APIs

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='EE'), 2025, 1, 0.00, NULL, TRUE
FROM users u WHERE u.email IN ('eestud1@example.com','eestud2@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='EE'), 2024, 2, 58.00, 3.70, TRUE
FROM users u WHERE u.email IN ('eestud3@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='ME'), 2025, 1, 0.00, NULL, TRUE
FROM users u WHERE u.email IN ('mestud1@example.com','mestud2@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='ME'), 2023, 3, 120.00, 4.00, TRUE
FROM users u WHERE u.email IN ('mestud3@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='BA'), 2025, 1, 0.00, NULL, TRUE
FROM users u WHERE u.email IN ('bastud1@example.com','bastud2@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id, (SELECT id FROM study_programs WHERE code='BA'), 2023, 3, 122.00, 4.20, TRUE
FROM users u WHERE u.email IN ('bastud3@example.com');
