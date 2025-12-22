USE skedjuli;

UPDATE academic_years SET is_active = FALSE;

INSERT INTO academic_years (year_code, start_date, end_date, enrollment_start, enrollment_end, is_active)
SELECT '2025/2026', '2025-10-01', '2026-09-30', '2025-09-01', '2025-10-15', TRUE
    WHERE NOT EXISTS (SELECT 1 FROM academic_years ay WHERE ay.year_code = '2025/2026');

UPDATE academic_years
SET is_active = TRUE
WHERE year_code = '2025/2026';
