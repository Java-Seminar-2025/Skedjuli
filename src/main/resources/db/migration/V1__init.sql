-- V1__init.sql
-- MySQL DDL only

-- Use InnoDB for FK support
-- You can remove these if Flyway manages schema separately
-- SET NAMES utf8mb4;
-- SET time_zone = '+00:00';

-- 1) users
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role INT NOT NULL, -- Role: STUDENT=1, PROFESSOR=2, ADMIN=3
    date_of_birth DATE NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username),
    UNIQUE KEY uk_users_email (email),
    KEY idx_users_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2) study_programs
CREATE TABLE study_programs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    duration_years INT NOT NULL,
    total_ects INT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_study_programs_code (code),
    UNIQUE KEY uk_study_programs_name (name),
    KEY idx_study_programs_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3) academic_years
CREATE TABLE academic_years (
    id BIGINT NOT NULL AUTO_INCREMENT,
    year_code VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    enrollment_start DATE NULL,
    enrollment_end DATE NULL,
    is_active BOOLEAN NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_academic_years_year_code (year_code),
    KEY idx_academic_years_active (is_active),
    CONSTRAINT chk_academic_year_dates CHECK (start_date < end_date),
    CONSTRAINT chk_enrollment_dates CHECK (
        enrollment_start IS NULL
        OR enrollment_end IS NULL
        OR enrollment_start <= enrollment_end
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4) lecturers (depends on users)
CREATE TABLE lecturers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    department VARCHAR(100) NULL,
    academic_title VARCHAR(100) NULL,
    office_location VARCHAR(30) NULL,
    phone_number VARCHAR(20) NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_lecturers_user_id (user_id),
    KEY idx_lecturers_active (is_active),
    CONSTRAINT fk_lecturers_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5) students (depends on users, study_programs)
CREATE TABLE students (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    study_program_id BIGINT NOT NULL,
    enrollment_year INT NOT NULL,
    current_year INT NOT NULL,
    average_grade DOUBLE NULL,
    total_ects_earned DOUBLE NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_students_user_id (user_id),
    KEY idx_students_study_program (study_program_id),
    KEY idx_students_active (is_active),
    CONSTRAINT fk_students_user FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_students_study_program FOREIGN KEY (study_program_id)
        REFERENCES study_programs(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6) courses (depends on lecturers, study_programs, academic_years)
CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    ects INT NOT NULL,
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    enrollment_limit INT NULL,
    lecturer_id BIGINT NULL,
    study_program_id BIGINT NULL,
    academic_year_id BIGINT NULL,
    semester INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_courses_code (code),
    KEY idx_courses_lecturer (lecturer_id),
    KEY idx_courses_study_program (study_program_id),
    KEY idx_courses_academic_year (academic_year_id),
    KEY idx_courses_semester (semester),
    CONSTRAINT fk_courses_lecturer FOREIGN KEY (lecturer_id)
        REFERENCES lecturers(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_courses_study_program FOREIGN KEY (study_program_id)
        REFERENCES study_programs(id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_courses_academic_year FOREIGN KEY (academic_year_id)
        REFERENCES academic_years(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7) course_req (CourseRequirementEntity) depends on courses
CREATE TABLE course_req (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    req_course_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_req_pair (course_id, req_course_id),
    KEY idx_course_req_course (course_id),
    KEY idx_course_req_req_course (req_course_id),
    CONSTRAINT fk_course_req_course FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_course_req_required FOREIGN KEY (req_course_id)
        REFERENCES courses(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 8) enrollment_forms (depends on students, academic_years, users(approved_by))
CREATE TABLE enrollment_forms (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    academic_year_id BIGINT NOT NULL,
    semester INT NOT NULL,
    status INT NOT NULL, -- EnrollmentFormStatus: 1..4
    submitted_at DATETIME NULL,
    approved_by BIGINT NULL,
    approved_at DATETIME NULL,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_enrollment_forms_student (student_id),
    KEY idx_enrollment_forms_academic_year (academic_year_id),
    KEY idx_enrollment_forms_status (status),
    CONSTRAINT fk_enrollment_forms_student FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_enrollment_forms_academic_year FOREIGN KEY (academic_year_id)
        REFERENCES academic_years(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_enrollment_forms_approved_by FOREIGN KEY (approved_by)
        REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9) enrollment_form_items (depends on enrollment_forms, courses)
CREATE TABLE enrollment_form_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    enrollment_form_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status INT NOT NULL, -- EnrollmentFormItemStatus: 1..3
    rejection_reason VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_enrollment_form_items (enrollment_form_id, course_id),
    KEY idx_enrollment_form_items_form (enrollment_form_id),
    KEY idx_enrollment_form_items_course (course_id),
    KEY idx_enrollment_form_items_status (status),
    CONSTRAINT fk_enrollment_form_items_form FOREIGN KEY (enrollment_form_id)
        REFERENCES enrollment_forms(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_enrollment_form_items_course FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10) completed_courses (depends on students, courses, academic_years)
CREATE TABLE completed_courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    academic_year_id BIGINT NULL,
    grade INT NULL,
    completion_date DATE NULL,
    created_at DATE NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id),
    UNIQUE KEY uk_completed_courses_student_course (student_id, course_id),
    KEY idx_completed_courses_student (student_id),
    KEY idx_completed_courses_course (course_id),
    KEY idx_completed_courses_academic_year (academic_year_id),
    CONSTRAINT fk_completed_courses_student FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_completed_courses_course FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_completed_courses_academic_year FOREIGN KEY (academic_year_id)
        REFERENCES academic_years(id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
