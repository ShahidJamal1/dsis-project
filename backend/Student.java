package com.example.scv.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String rollNo;

    private String course;

    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @Builder.Default
    private List<String> projects = new ArrayList<>();

    @Builder.Default
    private List<String> achievements = new ArrayList<>();

    @Builder.Default
    private List<Certificate> certificates = new ArrayList<>();
}
