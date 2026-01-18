USE skedjuli;

INSERT INTO courses
(code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT
    v.code,
    v.name,
    NULL,
    v.ects,
    TRUE,
    NULL,
    NULL,
    (SELECT id FROM study_programs WHERE code = 'CS'),
    NULL,
    v.semester,
    TRUE
FROM (
         -- ---------- SEMESTER 1 ----------
         SELECT 'SRC103','Introduction to Programming',7,1 UNION ALL
         SELECT 'SRC150','Introduction to Computing',6,1 UNION ALL
         SELECT 'SRC132','Web Development Basics',6,1 UNION ALL
         SELECT 'SRC104','Digital Systems',6,1 UNION ALL
         SELECT 'SRC101','Linear Algebra',6,1 UNION ALL

         -- ---------- SEMESTER 2 ----------
         SELECT 'SRC116','Computer Networks',5,2 UNION ALL
         SELECT 'SRC109','Programming Methods and Abstractions',7,2 UNION ALL
         SELECT 'SRC199','Linux Practicum',2,2 UNION ALL
         SELECT 'SRC106','Mathematical Analysis',6,2 UNION ALL
         SELECT 'SRC102','Physics for Computing',5,2 UNION ALL
         SELECT 'SRC110','Technical English',4,2 UNION ALL

         -- ---------- SEMESTER 3 ----------
         SELECT 'SRC113','Databases',6,3 UNION ALL
         SELECT 'SRC107','Operating Systems',5,3 UNION ALL
         SELECT 'SRC115','Data Structures and Algorithms',6,3 UNION ALL
         SELECT 'SRC119','Object-Oriented Programming',7,3 UNION ALL
         SELECT 'SRC112','Applied and Numerical Mathematics',6,3 UNION ALL

         -- ---------- SEMESTER 4 ----------
         SELECT 'SRC126','Web Application Development',5,4 UNION ALL
         SELECT 'SRC199M','Mobile Application Development',4,4 UNION ALL
         SELECT 'SRC118','Web UI Programming',5,4 UNION ALL
         SELECT 'SRC137','Linux Tools and Commands',5,4 UNION ALL
         SELECT 'SRC120','SQL Practicum',3,4 UNION ALL
         SELECT 'SRC117','Discrete Mathematics',6,4 UNION ALL
         SELECT 'SRC111','Economics and Business Organization',2,4 UNION ALL

         -- ---------- SEMESTER 5 ----------
         SELECT 'SRC145','Professional Internship',8,5 UNION ALL

         -- ---------- SEMESTER 6 ----------
         SELECT 'SRC146','Final Paper',12,6
     ) v(code,name,ects,semester);