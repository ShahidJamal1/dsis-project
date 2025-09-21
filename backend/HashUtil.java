package com.example.scv.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class HashUtil {

    public static String sha256Hex(Path file) throws IOException {
        try (InputStream is = Files.newInputStream(file)) {
            return sha256Hex(is);
        }
    }

    public static String sha256Hex(InputStream is) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (DigestInputStream dis = new DigestInputStream(is, md)) {
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1) {
                    // reading
                }
            }
            return "0x" + Hex.encodeHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
