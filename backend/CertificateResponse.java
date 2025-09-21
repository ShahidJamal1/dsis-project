package com.example.scv.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateResponse {
    private String fileName;
    private String sha256Hash;
    private String transactionHash;
    private boolean verified;
    private Instant uploadedAt;
}
