USE skedjuli;

-- EE prereqs
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SEL101' WHERE c.code='SEL105';

INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SEL104' WHERE c.code='SEL106';

-- ME prereqs
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SST101' WHERE c.code='SST105';

INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SST102' WHERE c.code='SST106';

-- BA prereqs
INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SPA101' WHERE c.code='SPA105';

INSERT IGNORE INTO course_req (course_id, req_course_id)
SELECT c.id, r.id FROM courses c JOIN courses r ON r.code='SPA103' WHERE c.code='SPA104';
