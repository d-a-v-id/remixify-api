package org.dvd.remixifyapi.storage.controller;

import lombok.RequiredArgsConstructor;
import org.dvd.remixifyapi.storage.service.StorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @PostMapping("/upload/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        String fileKey = storageService.uploadFile(file, StorageService.FileType.AVATAR);
        return ResponseEntity.ok(Map.of("fileKey", fileKey));
    }

    @PostMapping("/upload/recipe")
    public ResponseEntity<Map<String, String>> uploadRecipe(@RequestParam("file") MultipartFile file) throws IOException {
        String fileKey = storageService.uploadFile(file, StorageService.FileType.RECIPE);
        return ResponseEntity.ok(Map.of("fileKey", fileKey));
    }

    @GetMapping("/download/{fileKey}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileKey) throws IOException {
        byte[] data = storageService.downloadFile(fileKey);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileKey.substring(fileKey.lastIndexOf("/") + 1) + "\"")
                .body(resource);
    }

    @DeleteMapping("/{fileKey}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileKey) throws IOException {
        storageService.deleteFile(fileKey);
        return ResponseEntity.ok().build();
    }
}