USE skedjuli;

-- Student in year 2: completed semester 1 set
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id,
       CASE c.code
         WHEN 'SRC105' THEN 4
         WHEN 'SRC106A' THEN 3
         WHEN 'SRC107A' THEN 5
         WHEN 'SRC108' THEN 4
         WHEN 'SRC109A' THEN 5
         ELSE 3
       END AS grade,
       '2025-02-10' AS completion_date,
       NULL AS academic_year_id
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC106A','SRC107A','SRC108','SRC109A')
WHERE u.email IN ('stud3@example.com','stud5@example.com');

-- Year 3 students: completed more courses including DS/Algo/DB/OS/Networks
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id,
       CASE c.code
         WHEN 'SRC110A' THEN 4
         WHEN 'SRC111A' THEN 3
         WHEN 'SRC112' THEN 4
         WHEN 'SRC116A' THEN 5
         WHEN 'SRC117A' THEN 4
         WHEN 'SRC118A' THEN 5
         WHEN 'SRC119A' THEN 4
         WHEN 'SRC120A' THEN 4
         ELSE 3
       END AS grade,
       '2025-09-20' AS completion_date,
       NULL AS academic_year_id
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC110A','SRC111A','SRC112','SRC116A','SRC117A','SRC118A','SRC119A','SRC120A')
WHERE u.email IN ('stud6@example.com','stud8@example.com');

-- Add some completions for approved semester-1 students in current year to test partial progress
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id, 5, '2026-01-15',
       (SELECT id FROM academic_years WHERE year_code='2025/2026')
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC109A')
WHERE u.email IN ('stud1@example.com','stud2@example.com');
