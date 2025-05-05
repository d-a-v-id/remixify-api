package org.dvd.remixifyapi.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    @Value("${app.upload.dir:${user.home}/.remixify/uploads}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file, FileType fileType) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String relativePath = fileType.getPrefix() + fileName;
        Path targetPath = Paths.get(uploadDir, relativePath);
        
        // Create parent directories if they don't exist
        Files.createDirectories(targetPath.getParent());
        
        // Save the file
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        return relativePath;
    }

    @Override
    public byte[] downloadFile(String fileKey) throws IOException {
        Path filePath = Paths.get(uploadDir, fileKey);
        return Files.readAllBytes(filePath);
    }

    @Override
    public void deleteFile(String fileKey) throws IOException {
        Path filePath = Paths.get(uploadDir, fileKey);
        Files.deleteIfExists(filePath);
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }
} 