-- V4__seed_enrollment_form.sql
-- Ensure at least one enrollment form exists for API testing

-- Enrollment form for stud1, semester 1, status PENDING(1)
INSERT INTO enrollment_forms (
    student_id,
    academic_year_id,
    semester,
    status,
    submitted_at,
    approved_by,
    approved_at,
    is_locked
)
VALUES (
    (SELECT id FROM students WHERE user_id = (SELECT id FROM users WHERE username='stud1')),
    (SELECT id FROM academic_years WHERE year_code='2025/2026'),
    1,
    1,
    CURRENT_TIMESTAMP,
    NULL,
    NULL,
    FALSE
);

-- Items: CS101 pending, CS102 approved
INSERT INTO enrollment_form_items (enrollment_form_id, course_id, status, rejection_reason)
VALUES
(
  (SELECT id FROM enrollment_forms
     WHERE student_id = (SELECT id FROM students WHERE user_id = (SELECT id FROM users WHERE username='stud1'))
       AND academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
       AND semester = 1
   ORDER BY id DESC LIMIT 1),
  (SELECT id FROM courses WHERE code='CS101'),
  1,
  NULL
),
(
  (SELECT id FROM enrollment_forms
     WHERE student_id = (SELECT id FROM students WHERE user_id = (SELECT id FROM users WHERE username='stud1'))
       AND academic_year_id = (SELECT id FROM academic_years WHERE year_code='2025/2026')
       AND semester = 1
   ORDER BY id DESC LIMIT 1),
  (SELECT id FROM courses WHERE code='CS102'),
  2,
  NULL
);
