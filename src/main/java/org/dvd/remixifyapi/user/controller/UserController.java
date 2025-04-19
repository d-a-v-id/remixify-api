package org.dvd.remixifyapi.user.controller;

import java.util.List;
import java.util.Map;

import org.dvd.remixifyapi.storage.service.FileStorageService;
import org.dvd.remixifyapi.user.dto.UserDto;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final FileStorageService fileStorageService;
    private final UserService userService;

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
                .body(
                        Map.of(
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

    @PostMapping("/{username}/avatar")
    public ResponseEntity<Map<String, String>> updateProfilePicture(
            @PathVariable String username, @RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Please upload an image file"));
            }

            String avatarPath = fileStorageService.storeAvatar(image, username);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Profile picture updated successfully",
                            "username", username,
                            "avatarPath", avatarPath));
        } catch (Exception e) {
            log.error("Error updating profile picture: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update profile picture"));
        }
    }
}
