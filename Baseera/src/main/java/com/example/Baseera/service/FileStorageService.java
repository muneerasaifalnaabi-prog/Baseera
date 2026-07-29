package com.example.Baseera.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Handles the actual bytes. The DB never stores file content — only the
 * path returned by store(...) is persisted on Attachment.storedFilePath.
 *
 * application.properties:
 *   app.upload.dir=uploads
 */
@Service
public class FileStorageService {

    private final Path root;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload directory: " + root, e);
        }
    }

    /**
     * Saves the file under a random UUID-prefixed name (never trust the
     * client-supplied filename directly) and returns the path to store
     * in Attachment.storedFilePath.
     */
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store an empty file");
        }

        String originalName = Path.of(file.getOriginalFilename() == null
                        ? "file"
                        : file.getOriginalFilename())
                .getFileName()
                .toString();

        String storedName = UUID.randomUUID() + "_" + originalName;
        Path target = root.resolve(storedName).normalize();

        if (!target.getParent().equals(root)) {
            throw new IllegalArgumentException("Invalid file path");
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + originalName, e);
        }

        return target.toString();
    }

    /** Used by AttachmentAnalysisService when /analyze is triggered. */
    public byte[] loadAsBytes(String storedFilePath) {
        try {
            return Files.readAllBytes(Paths.get(storedFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored file: " + storedFilePath, e);
        }
    }

    public void delete(String storedFilePath) {
        try {
            Files.deleteIfExists(Paths.get(storedFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + storedFilePath, e);
        }
    }

    /** Extracts just the original filename portion from a MultipartFile, safely. */
    public String extractOriginalFileName(MultipartFile file) {
        return Path.of(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                .getFileName()
                .toString();
    }
}
