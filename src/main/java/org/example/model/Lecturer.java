package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lecturers")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_id", nullable = false)
    public Long userId;

    @Column(name = "department", length = 100)
    public String department;

    @Column(name = "academic_title", length = 100)
    public String academicTitle;

    @Column(name = "office_location", length = 30)
    public String officeLocation;

    @Column(name = "phone_number", length = 20)
    public String phoneNumber;

    @Column(name = "is_active")
    public Boolean isActive = true;
}
