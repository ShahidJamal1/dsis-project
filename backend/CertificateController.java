package com.example.scv.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.scv.domain.Certificate;
import com.example.scv.dto.CertificateResponse;
import com.example.scv.dto.VerificationResponse;
import com.example.scv.service.CertificateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Validated
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping(path = "/{id}/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificateResponse> upload(@PathVariable("id") String studentId,
                                                      @RequestParam("file") MultipartFile file) throws IOException {
        Certificate cert = certificateService.uploadCertificate(studentId, file);
        return ResponseEntity.ok(CertificateResponse.builder()
                .fileName(cert.getFileName())
                .sha256Hash(cert.getSha256Hash())
                .transactionHash(cert.getTransactionHash())
                .verified(cert.isVerified())
                .uploadedAt(cert.getUploadedAt())
                .build());
    }

    @GetMapping("/{id}/certificates/{certName}/verify")
    public ResponseEntity<VerificationResponse> verify(@PathVariable("id") String studentId,
                                                       @PathVariable("certName") String certName) throws IOException {
        boolean verified = certificateService.verifyCertificate(studentId, certName);
        return ResponseEntity.ok(VerificationResponse.builder()
                .verified(verified)
                .message(verified ? "✅ Verified" : "❌ Not Verified")
                .build());
    }
}
