package com.example.scv.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    private String fileName;
    private String sha256Hash; // hex
    private String transactionHash; // blockchain tx hash
    private String storagePath; // local storage path of the uploaded file
    private boolean verified; // computed status
    private Instant uploadedAt;
}
