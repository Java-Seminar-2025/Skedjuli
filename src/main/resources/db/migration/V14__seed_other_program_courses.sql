USE skedjuli;

-- Courses for EE (SELxxx), ME (SSTxxx), BA (SPAxxx)
-- Attach to academic year 2025/2026, with lecturers from their departments.

-- ---------- EE ----------
INSERT INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT v.code, v.name, v.description, v.ects, v.is_mandatory, v.enrollment_limit,
       v.lecturer_id,
       (SELECT id FROM study_programs WHERE code='EE'),
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       v.semester,
       TRUE
FROM (
  SELECT 'SEL101' AS code, 'Circuit Analysis I' AS name, 'Ohm/Kirchhoff, DC circuits' AS description, 6 AS ects, TRUE AS is_mandatory, 150 AS enrollment_limit,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof1@example.com') AS lecturer_id, 1 AS semester
  UNION ALL SELECT 'SEL102','Engineering Mathematics','Calculus for engineers',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof2@example.com'),1
  UNION ALL SELECT 'SEL103','Digital Electronics','Logic gates, flip-flops',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof2@example.com'),1
  UNION ALL SELECT 'SEL104','Signals and Systems I','Basics of signals, LTI systems',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof1@example.com'),2
  UNION ALL SELECT 'SEL105','Circuit Analysis II','AC circuits, phasors',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof1@example.com'),2
  UNION ALL SELECT 'SEL106','Embedded Systems Basics','MCU basics, IO, timers',5,FALSE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='eeprof2@example.com'),3
) v
WHERE NOT EXISTS (SELECT 1 FROM courses c WHERE c.code = v.code);

-- ---------- ME ----------
INSERT INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT v.code, v.name, v.description, v.ects, v.is_mandatory, v.enrollment_limit,
       v.lecturer_id,
       (SELECT id FROM study_programs WHERE code='ME'),
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       v.semester,
       TRUE
FROM (
  SELECT 'SST101' AS code, 'Engineering Mechanics I' AS name, 'Statics and basics of forces' AS description, 6 AS ects, TRUE AS is_mandatory, 150 AS enrollment_limit,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof1@example.com') AS lecturer_id, 1 AS semester
  UNION ALL SELECT 'SST102','Materials Science','Metals, polymers, composites',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof2@example.com'),1
  UNION ALL SELECT 'SST103','Technical Drawing','CAD basics, projections',5,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof1@example.com'),1
  UNION ALL SELECT 'SST104','Thermodynamics I','Energy, heat, cycles basics',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof2@example.com'),2
  UNION ALL SELECT 'SST105','Engineering Mechanics II','Dynamics, motion, work-energy',6,TRUE,150,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof1@example.com'),2
  UNION ALL SELECT 'SST106','Manufacturing Processes','Machining, forming, additive intro',5,FALSE,120,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='meprof2@example.com'),3
) v
WHERE NOT EXISTS (SELECT 1 FROM courses c WHERE c.code = v.code);

-- ---------- BA ----------
INSERT INTO courses (code, name, description, ects, is_mandatory, enrollment_limit, lecturer_id, study_program_id, academic_year_id, semester, is_active)
SELECT v.code, v.name, v.description, v.ects, v.is_mandatory, v.enrollment_limit,
       v.lecturer_id,
       (SELECT id FROM study_programs WHERE code='BA'),
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       v.semester,
       TRUE
FROM (
  SELECT 'SPA101' AS code, 'Principles of Management' AS name, 'Organizations, leadership, planning' AS description, 6 AS ects, TRUE AS is_mandatory, 200 AS enrollment_limit,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof1@example.com') AS lecturer_id, 1 AS semester
  UNION ALL SELECT 'SPA102','Microeconomics','Supply/demand, markets',6,TRUE,200,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof2@example.com'),1
  UNION ALL SELECT 'SPA103','Business Mathematics','Percentages, finance math basics',5,TRUE,200,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof2@example.com'),1
  UNION ALL SELECT 'SPA104','Accounting I','Balance sheet, income statement',6,TRUE,200,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof1@example.com'),2
  UNION ALL SELECT 'SPA105','Marketing Fundamentals','4Ps, segmentation, positioning',6,TRUE,200,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof1@example.com'),2
  UNION ALL SELECT 'SPA106','Business Communication','Email, presentations, negotiation',4,FALSE,200,
         (SELECT l.id FROM lecturers l JOIN users u ON u.id=l.user_id WHERE u.email='baprof2@example.com'),2
) v
WHERE NOT EXISTS (SELECT 1 FROM courses c WHERE c.code = v.code);
