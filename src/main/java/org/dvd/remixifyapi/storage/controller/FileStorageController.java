package org.dvd.remixifyapi.storage.controller;

import java.util.Map;

import org.dvd.remixifyapi.storage.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping("/avatars/{username}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String username) {
        Resource file = fileStorageService.loadAvatar(username);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
            .body(file);
    }

    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<Resource> getRecipeImage(@PathVariable Long recipeId) {
        Resource file = fileStorageService.loadRecipeImage(recipeId);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
            .body(file);
    }

    @GetMapping("/avatars/{username}/url")
    public ResponseEntity<Map<String, String>> getAvatarUrl(@PathVariable String username) {
        String url = fileStorageService.getAvatarImageUrl(username);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/recipes/{recipeId}/url")
    public ResponseEntity<Map<String, String>> getRecipeImageUrl(@PathVariable Long recipeId) {
        String url = fileStorageService.getRecipeImageUrl(recipeId);
        return ResponseEntity.ok(Map.of("url", url));
    }
}