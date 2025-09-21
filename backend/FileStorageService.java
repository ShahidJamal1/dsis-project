package com.example.scv.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path storageDir;

    public FileStorageService(@Value("${file.storageDir}") String storageDir) throws IOException {
        this.storageDir = Path.of(storageDir).toAbsolutePath().normalize();
        Files.createDirectories(this.storageDir);
    }

    public String storeForStudent(String studentId, MultipartFile file) throws IOException {
        String cleanedFileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path studentDir = storageDir.resolve(studentId);
        Files.createDirectories(studentDir);
        Path target = studentDir.resolve(cleanedFileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public Path resolve(String path) {
        return Path.of(path);
    }
}
