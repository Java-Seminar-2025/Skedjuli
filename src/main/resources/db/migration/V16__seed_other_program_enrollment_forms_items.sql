USE skedjuli;

-- Form status: PENDING=1, APPROVED=2, REJECTED=3, LOCKED=4
-- Item status: PENDING=1, APPROVED=2, REJECTED=3

-- Pick an approver (admin if exists, else first admin-like user)
SET @approver_id := (SELECT id FROM users WHERE role=3 ORDER BY id LIMIT 1);
SET @ay_id := (SELECT id FROM academic_years WHERE year_code='2025/2026');

-- ---------- EE forms (semester 1) ----------
INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 2, NOW(), @approver_id, NOW(), TRUE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('eestud1@example.com','eestud3@example.com');

INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 1, NOW(), NULL, NULL, FALSE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('eestud2@example.com');

-- EE items
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SEL101','SEL102','SEL103')
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status=2 AND u.email IN ('eestud1@example.com','eestud3@example.com');

INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 1, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SEL101','SEL102')
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status=1 AND u.email='eestud2@example.com';

-- ---------- ME forms (semester 1) ----------
INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 2, NOW(), @approver_id, NOW(), TRUE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('mestud1@example.com','mestud3@example.com');

INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 3, NOW(), @approver_id, NOW(), FALSE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('mestud2@example.com');

-- ME items approved
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SST101','SST102','SST103')
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status=2 AND u.email IN ('mestud1@example.com','mestud3@example.com');

-- ME rejected item
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 3, 'Student not eligible for this semester'
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code='SST101'
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status=3 AND u.email='mestud2@example.com';

-- ---------- BA forms (semester 1) ----------
INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 4, NOW(), @approver_id, NOW(), TRUE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('bastud1@example.com');

INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 2, NOW(), @approver_id, NOW(), TRUE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('bastud3@example.com');

INSERT IGNORE INTO enrollment_forms (student_id, academic_year_id, semester, status, submitted_at, approved_by, approved_at, is_locked)
SELECT s.id, @ay_id, 1, 1, NOW(), NULL, NULL, FALSE
FROM students s JOIN users u ON u.id=s.user_id
WHERE u.email IN ('bastud2@example.com');

-- BA items
INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 2, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SPA101','SPA102','SPA103')
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status IN (2,4) AND u.email IN ('bastud1@example.com','bastud3@example.com');

INSERT IGNORE INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
SELECT ef.id, c.id, 1, NULL
FROM enrollment_forms ef
JOIN students s ON s.id=ef.student_id
JOIN users u ON u.id=s.user_id
JOIN courses c ON c.code IN ('SPA101','SPA102')
WHERE ef.academic_year_id=@ay_id AND ef.semester=1 AND ef.status=1 AND u.email='bastud2@example.com';
