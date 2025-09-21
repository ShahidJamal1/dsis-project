package com.example.scv.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StudentRegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String rollNo;

    @NotBlank
    private String course;

    private List<@NotBlank String> skills;
    private List<@NotBlank String> projects;
    private List<@NotBlank String> achievements;
}
