USE skedjuli;

-- Programming II requires Programming I
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC105'
WHERE c.code='SRC110A';

-- Data Structures requires Programming II
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC110A'
WHERE c.code='SRC116A';

-- Algorithms requires Data Structures
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC116A'
WHERE c.code='SRC117A';

-- Databases II requires Databases I
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC118A'
WHERE c.code='SRC123';

-- Web App Dev requires Web Basics + Programming II
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC113A'
WHERE c.code='SRC122';

INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC110A'
WHERE c.code='SRC122';

-- Internship requires Software Engineering
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC121'
WHERE c.code='SRC145A';

-- Final Paper requires Internship
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code='SRC145A'
WHERE c.code='SRC146A';
