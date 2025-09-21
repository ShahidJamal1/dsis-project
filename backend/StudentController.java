package com.example.scv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scv.domain.Student;
import com.example.scv.dto.StudentProfileResponse;
import com.example.scv.dto.StudentRegisterRequest;
import com.example.scv.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<StudentProfileResponse> register(@Valid @RequestBody StudentRegisterRequest req) {
        Student s = studentService.register(req);
        return ResponseEntity.ok(StudentProfileResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .rollNo(s.getRollNo())
                .course(s.getCourse())
                .skills(s.getSkills())
                .projects(s.getProjects())
                .achievements(s.getAchievements())
                .certificates(s.getCertificates())
                .build());
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<StudentProfileResponse> profile(@PathVariable String id) {
        Student s = studentService.getOrThrow(id);
        return ResponseEntity.ok(StudentProfileResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .rollNo(s.getRollNo())
                .course(s.getCourse())
                .skills(s.getSkills())
                .projects(s.getProjects())
                .achievements(s.getAchievements())
                .certificates(s.getCertificates())
                .build());
    }
}
