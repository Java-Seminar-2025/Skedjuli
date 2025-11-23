-- V1__init.sql

CREATE DATABASE IF NOT EXISTS skedjuli;
USE skedjuli;

CREATE USER IF NOT EXISTS 'skeduser'@'%' IDENTIFIED BY 'skedpass';
GRANT ALL PRIVILEGES ON skedjuli.* TO 'skeduser'@'%';
FLUSH PRIVILEGES;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role INT NOT NULL,
    date_of_birth DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE study_programs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_years INT NOT NULL,
    total_ects INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE academic_years (
    id INT AUTO_INCREMENT PRIMARY KEY,
    year_code VARCHAR(20) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    enrollment_start DATE,
    enrollment_end DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lecturers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    /*employee_id INT NOT NULL UNIQUE,*/
    department VARCHAR(100),
    academic_title VARCHAR(100),
    office_location VARCHAR(30),
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_lecturer_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    /*student_id INT NOT NULL UNIQUE,*/
    study_program_id INT NOT NULL,
    enrollment_year INT NOT NULL,
    current_year INT NOT NULL,
    total_ects_earned DECIMAL(5,2),
    average_grade DECIMAL(3,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_student_program FOREIGN KEY (study_program_id) REFERENCES study_programs(id)
);

CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    ects INT NOT NULL,
    is_mandatory BOOLEAN DEFAULT TRUE,
    enrollment_limit INT,
    lecturer_id INT,
    study_program_id INT,
    academic_year_id INT,
    semester INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturers(id),
    CONSTRAINT fk_course_program FOREIGN KEY (study_program_id) REFERENCES study_programs(id),
    CONSTRAINT fk_course_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id)
);

CREATE TABLE course_req (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    req_course_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_req_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_req_req_course FOREIGN KEY (req_course_id) REFERENCES courses(id)
);

CREATE TABLE enrollment_forms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    academic_year_id INT NOT NULL,
    semester INT NOT NULL,
    status INT NOT NULL,
    submitted_at TIMESTAMP NULL,
    approved_by INT NULL,
    approved_at TIMESTAMP NULL,
    is_locked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_form_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_form_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id),
    CONSTRAINT fk_form_approved_by FOREIGN KEY (approved_by) REFERENCES lecturers(id)
);

CREATE TABLE enrollment_form_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_form_id INT NOT NULL,
    course_id INT NOT NULL,
    status INT NOT NULL,
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_form FOREIGN KEY (enrollment_form_id) REFERENCES enrollment_forms(id),
    CONSTRAINT fk_item_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE completed_courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    grade INT,
    completion_date DATE,
    academic_year_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_completed_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_completed_course FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT fk_completed_academic_year FOREIGN KEY (academic_year_id) REFERENCES academic_years(id)
);