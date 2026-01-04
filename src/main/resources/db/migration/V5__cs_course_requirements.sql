USE skedjuli;

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC103'
WHERE c.code = 'SRC109';

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC109'
WHERE c.code = 'SRC115';

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC109'
WHERE c.code = 'SRC119';

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC109'
WHERE c.code = 'SRC113';

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC113'
WHERE c.code = 'SRC126';

INSERT INTO course_req (course_id, req_course_id)
SELECT c.id, r.id
FROM courses c
JOIN courses r ON r.code = 'SRC145'
WHERE c.code = 'SRC146';
