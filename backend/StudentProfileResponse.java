package com.example.scv.dto;

import java.util.List;

import com.example.scv.domain.Certificate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentProfileResponse {
    private String id;
    private String name;
    private String rollNo;
    private String course;
    private List<String> skills;
    private List<String> projects;
    private List<String> achievements;
    private List<Certificate> certificates;
}
