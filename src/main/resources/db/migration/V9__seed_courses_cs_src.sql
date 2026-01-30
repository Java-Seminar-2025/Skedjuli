USE skedjuli;

INSERT IGNORE INTO courses
(code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT v.code, v.name, v.description, v.ects, v.is_mandatory, v.enrollment_limit,
       v.lecturer_id,
       (SELECT id FROM study_programs WHERE code='CS'),
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       v.semester,
       TRUE
FROM (
  -- Semester 1
  SELECT 'SRC105' AS code, 'Programming Fundamentals I' AS name, 'Basics: variables, control flow, functions' AS description, 7 AS ects, TRUE AS is_mandatory, 120 AS enrollment_limit,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof1@example.com') AS lecturer_id, 1 AS semester
  UNION ALL SELECT 'SRC106A','Mathematical Analysis I','Limits, derivatives, integrals',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),1
  UNION ALL SELECT 'SRC107A','Linear Algebra I','Vectors, matrices, linear systems',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),1
  UNION ALL SELECT 'SRC108','Digital Logic','Boolean algebra, combinational/sequential circuits',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof3@example.com'),1
  UNION ALL SELECT 'SRC109A','Technical English I','Reading/writing for CS',4,TRUE,200,
         NULL,1

  -- Semester 2
  UNION ALL SELECT 'SRC110A','Programming Fundamentals II','OOP intro + testing basics',7,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof1@example.com'),2
  UNION ALL SELECT 'SRC111A','Discrete Mathematics','Logic, sets, relations, graphs',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),2
  UNION ALL SELECT 'SRC113A','Web Basics','HTML/CSS/JS fundamentals',6,TRUE,150,
         NULL,2
  UNION ALL SELECT 'SRC114','Linux Basics','Shell, processes, permissions',3,TRUE,200,
         NULL,2
  UNION ALL SELECT 'SRC115A','Technical English II','Presentations + documentation',3,FALSE,200,
         NULL,2

  -- Semester 3
  UNION ALL SELECT 'SRC116A','Data Structures','Lists, stacks, queues, trees',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof1@example.com'),3
  UNION ALL SELECT 'SRC117A','Algorithms','Complexity, sorting, searching, graphs',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof1@example.com'),3
  UNION ALL SELECT 'SRC118A','Databases I','Relational model, SQL, normalization concepts',6,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof3@example.com'),3
  UNION ALL SELECT 'SRC119A','Operating Systems','Processes, threads, scheduling, memory',5,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof3@example.com'),3
  UNION ALL SELECT 'SRC120A','Computer Networks','TCP/IP, routing, basic security',5,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),3

  -- Semester 4
  UNION ALL SELECT 'SRC121','Software Engineering','Requirements, design, teamwork, CI basics',5,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),4
  UNION ALL SELECT 'SRC122','Web Application Development','REST, MVC, auth basics',5,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof1@example.com'),4
  UNION ALL SELECT 'SRC123','Databases II','Indexes, transactions, performance',5,TRUE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof3@example.com'),4
  UNION ALL SELECT 'SRC124','Mobile Development','Android basics, lifecycle, storage',4,FALSE,90,
         NULL,4
  UNION ALL SELECT 'SRC125','Web UI Programming','SPA basics, state, forms',5,FALSE,120,
         NULL,4
  UNION ALL SELECT 'SRC126A','Security Fundamentals','Threats, crypto basics, OWASP intro',4,FALSE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof3@example.com'),4

  -- Semester 5
  UNION ALL SELECT 'SRC145A','Professional Internship','Industry internship placement',8,TRUE,999,
         NULL,5
  UNION ALL SELECT 'SRC140','Cloud Basics','Containers, deployments, monitoring',4,FALSE,120,
         NULL,5
  UNION ALL SELECT 'SRC141','DevOps Practices','CI/CD, IaC intro, observability',4,FALSE,120,
         NULL,5

  -- Semester 6
  UNION ALL SELECT 'SRC146A','Final Paper','Capstone project + report',12,TRUE,999,
         NULL,6
  UNION ALL SELECT 'SRC150A','Advanced Topics','Elective: ML/AI/security topics',6,FALSE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='prof2@example.com'),6
) v;
