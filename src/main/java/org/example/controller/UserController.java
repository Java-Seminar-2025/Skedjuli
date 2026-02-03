package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.UserCreateRequest;
import org.example.model.dto.request.patch.UserPatchRequest;
import org.example.model.dto.response.UserResponse;
import org.example.service.domain.UserDomainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDomainService service;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse created = service.createUser(request);
        return ResponseEntity
                .created(URI.create("/api/users/" + created.id()))
                .body(created);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserResponseByEmail(email));
    }

    @PatchMapping("/{email}")
    public ResponseEntity<UserResponse> patchUser(@PathVariable String email, @Valid @RequestBody UserPatchRequest request) {
        return ResponseEntity.ok( service.patchUser(email, request));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        service.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
