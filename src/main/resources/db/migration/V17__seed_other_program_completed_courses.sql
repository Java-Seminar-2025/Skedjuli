USE skedjuli;

SET @ay_id := (SELECT id FROM academic_years WHERE year_code='2025/2026');

-- EE: year-2 student has some completions
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id,
       CASE c.code WHEN 'SEL101' THEN 4 WHEN 'SEL102' THEN 3 WHEN 'SEL103' THEN 5 ELSE 3 END,
       '2025-07-01', NULL
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SEL101','SEL102','SEL103')
WHERE u.email='eestud3@example.com';

-- ME: year-3 student has more completions
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id,
       CASE c.code WHEN 'SST101' THEN 4 WHEN 'SST102' THEN 4 WHEN 'SST103' THEN 5 WHEN 'SST105' THEN 4 ELSE 3 END,
       '2025-09-10', NULL
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SST101','SST102','SST103','SST105')
WHERE u.email='mestud3@example.com';

-- BA: year-3 student completions
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id,
       CASE c.code WHEN 'SPA101' THEN 5 WHEN 'SPA102' THEN 4 WHEN 'SPA103' THEN 4 WHEN 'SPA104' THEN 5 ELSE 3 END,
       '2025-06-15', NULL
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SPA101','SPA102','SPA103','SPA104')
WHERE u.email='bastud3@example.com';

-- Add a couple of "current year" completions to test partial progress
INSERT IGNORE INTO completed_courses (student_id, course_id, grade, completion_date, academic_year_id)
SELECT s.id, c.id, 5, '2026-01-10', @ay_id
FROM students s
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SEL101','SST101','SPA101')
WHERE u.email IN ('eestud1@example.com','mestud1@example.com','bastud1@example.com');
