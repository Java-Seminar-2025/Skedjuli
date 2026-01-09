package org.example.model.dto.request.patch;

import java.time.LocalDate;

public record UserPatchRequest (
        String firstName,
        String lastName,
        String email,
        String password,
        LocalDate dateOfBirth
) {}
