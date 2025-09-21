package com.example.scv.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.scv.domain.Student;
import com.example.scv.dto.StudentRegisterRequest;
import com.example.scv.exception.NotFoundException;
import com.example.scv.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Student register(StudentRegisterRequest req) {
        Student student = Student.builder()
            .name(req.getName())
            .rollNo(req.getRollNo())
            .course(req.getCourse())
            .skills(Optional.ofNullable(req.getSkills()).orElseGet(java.util.List::of))
            .projects(Optional.ofNullable(req.getProjects()).orElseGet(java.util.List::of))
            .achievements(Optional.ofNullable(req.getAchievements()).orElseGet(java.util.List::of))
            .build();
        return studentRepository.save(student);
    }

    public Student getOrThrow(String id) {
        return studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student not found: " + id));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
