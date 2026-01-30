USE skedjuli;

-- ---------- FORMS: Semester 1 ----------
INSERT IGNORE INTO enrollment_forms
(student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id,
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       1,
       2, -- APPROVED
       NOW(),
       (SELECT id FROM users WHERE email='admin1@example.com'),
       NOW(),
       TRUE
FROM students s
JOIN users u ON u.id=s.user_id
WHERE u.email IN ('stud1@example.com','stud2@example.com','stud3@example.com');

INSERT IGNORE INTO enrollment_forms
(student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id,
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       1,
       1, -- PENDING
       NOW(),
       NULL,
       NULL,
       FALSE
FROM students s
JOIN users u ON u.id=s.user_id
WHERE u.email IN ('stud4@example.com','stud5@example.com');

INSERT IGNORE INTO enrollment_forms
(student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id,
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       1,
       3, -- REJECTED
       NOW(),
       (SELECT id FROM users WHERE email='admin1@example.com'),
       NOW(),
       FALSE
FROM students s
JOIN users u ON u.id=s.user_id
WHERE u.email IN ('stud6@example.com');

-- ---------- ITEMS: Semester 1 (mix of approved/rejected/pending) ----------
-- For approved forms: approve most, reject one to test rejection_reason.
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC106A','SRC107A','SRC108','SRC109A')
WHERE ef.semester=1
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email IN ('stud1@example.com','stud2@example.com')
  AND ef.status=2;

-- One rejected item with reason
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 3, 'Enrollment limit reached'
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code='SRC108'
WHERE ef.semester=1
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email='stud3@example.com';

-- Remaining items for stud3 approved
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC106A','SRC107A','SRC109A')
WHERE ef.semester=1
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email='stud3@example.com'
  AND ef.status=2;

-- Pending forms: items pending
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 1, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC106A','SRC107A','SRC108')
WHERE ef.semester=1
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email IN ('stud4@example.com','stud5@example.com')
  AND ef.status=1;

-- Rejected form: items rejected (with reason)
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 3, 'Student not eligible for semester 1 enrollment'
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC105','SRC106A')
WHERE ef.semester=1
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email='stud6@example.com'
  AND ef.status=3;


-- ---------- FORMS: Semester 2 ----------
INSERT IGNORE INTO enrollment_forms
(student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id,
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       2,
       2, -- APPROVED
       NOW(),
       (SELECT id FROM users WHERE email='admin1@example.com'),
       NOW(),
       TRUE
FROM students s
JOIN users u ON u.id=s.user_id
WHERE u.email IN ('stud6@example.com','stud8@example.com');

INSERT IGNORE INTO enrollment_forms
(student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id,
       (SELECT id FROM academic_years WHERE year_code='2025/2026'),
       2,
       4, -- LOCKED
       NOW(),
       (SELECT id FROM users WHERE email='admin1@example.com'),
       NOW(),
       TRUE
FROM students s
JOIN users u ON u.id=s.user_id
WHERE u.email IN ('stud10@example.com');

-- Items for semester 2 approved/locked forms
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SRC110A','SRC111A','SRC112','SRC113A','SRC114')
WHERE ef.semester=2
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email IN ('stud6@example.com','stud8@example.com')
  AND ef.status IN (2,4);

-- Add one rejected item to locked form to test mixed outcomes
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 3, 'Prerequisite not met'
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code='SRC110A'
WHERE ef.semester=2
  AND ef.academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
  AND u.email='stud10@example.com'
  AND ef.status=4;
