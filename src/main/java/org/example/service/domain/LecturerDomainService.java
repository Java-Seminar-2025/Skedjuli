package org.example.service.domain;

import lombok.AllArgsConstructor;
import org.example.model.dto.request.create.LecturerCreateRequest;
import org.example.model.entity.LecturerEntity;
import org.example.model.entity.UserEntity;
import org.example.repository.LecturerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LecturerDomainService {

    private final LecturerRepository lecturerRepository;

    public void createLecturer(LecturerCreateRequest request) {
        var lecturer = new LecturerEntity();

        var uRef = new UserEntity();
        uRef.setId(request.userId());
        lecturer.setUser(uRef);

        lecturer.setDepartment(request.department());
        lecturer.setAcademicTitle(request.title());
        lecturer.setOfficeLocation(request.office());
        lecturer.setPhoneNumber(request.phone());
        lecturer.setIsActive(true);
        lecturer.setCreatedAt(LocalDateTime.now());

        lecturerRepository.save(lecturer);
    }
}
