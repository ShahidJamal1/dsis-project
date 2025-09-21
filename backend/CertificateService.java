package com.example.scv.service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.scv.domain.Certificate;
import com.example.scv.domain.Student;
import com.example.scv.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final StudentService studentService;
    private final FileStorageService fileStorageService;
    private final BlockchainService blockchainService;

    public Certificate uploadCertificate(String studentId, MultipartFile file) throws IOException {
        Student student = studentService.getOrThrow(studentId);
        String path = fileStorageService.storeForStudent(studentId, file);
        String sha256 = HashUtil.sha256Hex(file.getInputStream()); // hash of uploaded content
        String txHash = blockchainService.storeCertificateHash(studentId, file.getOriginalFilename(), sha256);

        Certificate cert = Certificate.builder()
                .fileName(file.getOriginalFilename())
                .sha256Hash(sha256)
                .transactionHash(txHash)
                .storagePath(path)
                .verified(false)
                .uploadedAt(Instant.now())
                .build();
        student.getCertificates().add(cert);
        studentService.save(student);
        return cert;
    }

    public boolean verifyCertificate(String studentId, String certName) throws IOException {
        Student student = studentService.getOrThrow(studentId);
        Optional<Certificate> maybe = student.getCertificates().stream()
                .filter(c -> c.getFileName().equals(certName))
                .findFirst();
        Certificate cert = maybe.orElseThrow(() -> new NotFoundException("Certificate not found: " + certName));

        String localHash = HashUtil.sha256Hex(fileStorageService.resolve(cert.getStoragePath()));
        Optional<String> chainHashOpt = blockchainService.getStoredHash(studentId, cert.getFileName());
        boolean verified = chainHashOpt.isPresent() && equalsHexInsensitive(localHash, chainHashOpt.get());
        cert.setVerified(verified);
        studentService.save(student);
        return verified;
    }

    private static boolean equalsHexInsensitive(String a, String b) {
        if (a == null || b == null) return false;
        return a.replaceFirst("^0x", "").equalsIgnoreCase(b.replaceFirst("^0x", ""));
    }
}
