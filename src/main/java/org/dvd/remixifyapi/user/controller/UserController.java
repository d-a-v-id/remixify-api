package org.dvd.remixifyapi.user.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.dvd.remixifyapi.recipe.dto.RecipeDto;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recommendation.RecommendationService;
import org.dvd.remixifyapi.recommendation.dto.RecommendationResponse;
import org.dvd.remixifyapi.storage.service.StorageService;
import org.dvd.remixifyapi.storage.util.FileStorageUtils;
import org.dvd.remixifyapi.user.dto.UserDto;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;
    private final StorageService storageService;

    @GetMapping
    public List<UserDto> getUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(UserDto::fromUser).toList();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "User created successfully",
                        "username",
                        user.getUsername()));
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Map<String, String>> updateUser(
            @PathVariable String username, @RequestBody UserDto userDto) {

        userService.updateUser(username, userDto);

        return ResponseEntity.ok(
                Map.of("message", "User updated successfully", "username", username));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(
                Map.of("message", "User deleted successfully", "username", username));
    }

    @GetMapping("/{username}/likes")
    @Transactional(readOnly = true)
    public ResponseEntity<List<RecipeDto>> getUserLikedRecipes(@PathVariable String username) {
        try {
            List<Recipe> likedRecipes = userService.getLikedRecipesByUsername(username);
            return ResponseEntity.ok(likedRecipes.stream()
                    .map(RecipeDto::fromRecipe)
                    .toList());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{username}/recommendations")
    public ResponseEntity<RecommendationResponse> recommend(
            @PathVariable String username,
            @RequestParam(defaultValue = "5") int limit) {
        RecommendationResponse response = recommendationService.recommend(username, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{username}/avatar")
    public ResponseEntity<Map<String, String>> updateProfilePicture(
            @PathVariable String username, @RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Please upload an image file"));
            }

            // Delete old avatar ONLY if it's not the default one
            User user = userService.getUser(username);
            if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()
                    && !user.getAvatarPath().equals(FileStorageUtils.DEFAULT_AVATAR_PATH)) {
                storageService.deleteFile(user.getAvatarPath());
            }

            String avatarPath = storageService.uploadFile(image, StorageService.FileType.AVATAR);
            userService.updateUserAvatarPath(username, avatarPath);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Profile picture updated successfully",
                            "username", username,
                            "avatarPath", FileStorageUtils.getFullAvatarImageUrl(avatarPath)));
        } catch (Exception e) {
            log.error("Error updating profile picture: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update profile picture"));
        }
    }
}
