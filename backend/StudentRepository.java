package com.example.scv.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.scv.domain.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByRollNo(String rollNo);
}
