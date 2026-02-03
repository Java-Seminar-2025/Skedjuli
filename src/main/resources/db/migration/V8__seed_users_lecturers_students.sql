USE skedjuli;

INSERT IGNORE INTO users (username, email, password_hash, first_name, last_name, role, date_of_birth)
VALUES
  ('admin1', 'admin1@example.com', '$2a$10$seedadminhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Ana', 'Admin', 3, '1990-01-10'),
  ('prof1',  'prof1@example.com',  '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Marko', 'Kovac', 2, '1980-03-22'),
  ('prof2',  'prof2@example.com',  '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Ivana', 'Horvat', 2, '1978-07-14'),
  ('prof3',  'prof3@example.com',  '$2a$10$seedprofhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Petar', 'Babic', 2, '1985-11-02'),

  ('stud1',  'stud1@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Luka', 'Maric', 1, '2003-02-05'),
  ('stud2',  'stud2@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Mia',  'Novak', 1, '2003-09-19'),
  ('stud3',  'stud3@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Ema',  'Ivic',  1, '2002-12-01'),
  ('stud4',  'stud4@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Noa',  'Basic', 1, '2004-06-11'),
  ('stud5',  'stud5@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Sara', 'Skoko', 1, '2002-04-28'),
  ('stud6',  'stud6@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Ivan', 'Jukic', 1, '2001-10-30'),
  ('stud7',  'stud7@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Klara','Pavic', 1, '2003-01-17'),
  ('stud8',  'stud8@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Toni', 'Knez',  1, '2002-08-08'),
  ('stud9',  'stud9@example.com',  '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Nika', 'Peric', 1, '2004-03-03'),
  ('stud10', 'stud10@example.com', '$2a$10$seedstudhashxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 'Filip','Varga', 1, '2001-05-25');

-- ---------- LECTURERS ----------
INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Computer Science', 'Assistant', 'A-201', '111-111', TRUE
FROM users u
WHERE u.email = 'prof1@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Computer Science', 'Senior Assistant', 'A-202', '222-222', TRUE
FROM users u
WHERE u.email = 'prof2@example.com';

INSERT IGNORE INTO lecturers (user_id, department, academic_title, office_location, phone_number, is_active)
SELECT u.id, 'Computer Science', 'Professor', 'A-203', '333-333', TRUE
FROM users u
WHERE u.email = 'prof3@example.com';

-- ---------- STUDENTS (attach to CS program) ----------
INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id,
       (SELECT id FROM study_programs WHERE code='CS'),
       2025, 1, 0.00, NULL, TRUE
FROM users u
WHERE u.email IN ('stud1@example.com','stud2@example.com','stud4@example.com','stud7@example.com','stud9@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id,
       (SELECT id FROM study_programs WHERE code='CS'),
       2024, 2, 55.00, 3.60, TRUE
FROM users u
WHERE u.email IN ('stud3@example.com','stud5@example.com','stud10@example.com');

INSERT IGNORE INTO students (user_id, study_program_id, enrollment_year, current_year, total_ects_earned, average_grade, is_active)
SELECT u.id,
       (SELECT id FROM study_programs WHERE code='CS'),
       2023, 3, 118.00, 4.10, TRUE
FROM users u
WHERE u.email IN ('stud6@example.com','stud8@example.com');
