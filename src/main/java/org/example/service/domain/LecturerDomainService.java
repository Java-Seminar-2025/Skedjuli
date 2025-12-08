package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.domain.entity.LecturerEntity;
import org.example.domain.entity.UserEntity;
import org.example.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LecturerDomainService {

    private final LecturerRepository lecturerRepository;

    public void createLecturer(UserEntity user, String department, String title, String office, String phone) {
        LecturerEntity lecturer = new LecturerEntity();
        lecturer.setUser(user);
        lecturer.setDepartment(department);
        lecturer.setAcademicTitle(title);
        lecturer.setOfficeLocation(office);
        lecturer.setPhoneNumber(phone);
        lecturer.setIsActive(true);
        lecturer.setCreatedAt(LocalDateTime.now());

        lecturerRepository.save(lecturer);
    }
}