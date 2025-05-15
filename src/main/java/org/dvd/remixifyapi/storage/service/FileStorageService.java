package org.dvd.remixifyapi.storage.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.service.RecipeService;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private static final String RECIPES_DIR = "uploads/recipes";
    private static final String AVATARS_DIR = "uploads/avatars";
    private static final String DEFAULT_AVATAR = AVATARS_DIR + "/default-avatar.webp";
    private static final String DEFAULT_RECIPE = RECIPES_DIR + "/default-recipe.webp";

    private final RecipeService recipeService;
    private final UserService userService;

    public String storeFile(MultipartFile file, String uploadDir) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            Path uploadPath = Path.of(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uploadDir + "/" + newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + newFileName, ex);
        }
    }

    public String storeAvatar(MultipartFile file, String username) {
        String avatarPath = getAvatarPath(username);
        try {
            if (!avatarPath.equals(DEFAULT_AVATAR)) {
                Files.deleteIfExists(Path.of(avatarPath));
            }

            String newAvatarUrlPath = storeFile(file, AVATARS_DIR);
            userService.updateUserAvatarPath(username, newAvatarUrlPath);
            return newAvatarUrlPath;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store avatar for user " + username, ex);
        }
    }

    public String storeRecipeImage(MultipartFile file, Long recipeId) {
        String recipePath = getRecipePath(recipeId);
        try {
            Path uploadPath = Path.of(RECIPES_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path targetLocation = Path.of(recipePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return recipePath;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store image for recipe " + recipeId, ex);
        }
    }

    public Resource loadAvatar(String username) {
        String avatarPath = getAvatarPath(username);
        try {
            Resource resource = loadFileAsResource(avatarPath);
            return resource.exists() ? resource : loadFileAsResource(DEFAULT_AVATAR);
        } catch (Exception e) {
            return loadFileAsResource(DEFAULT_AVATAR);
        }
    }

    public Resource loadRecipeImage(Long recipeId) {
        String recipePath = getRecipePath(recipeId);
        try {
            Resource resource = loadFileAsResource(recipePath);
            return resource.exists() ? resource : loadFileAsResource(DEFAULT_RECIPE);
        } catch (Exception e) {
            return loadFileAsResource(DEFAULT_RECIPE);
        }
    }

    private Resource loadFileAsResource(String filePath) {
        try {
            Path path = Path.of(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load file: " + filePath, e);
        }
    }

    private String getAvatarPath(String username) {
        User user = userService.getUser(username);
        if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
            return DEFAULT_AVATAR;
        }
        return user.getAvatarPath();
    }

    private String getRecipePath(Long recipeId) {
        Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);

        if (recipe.isEmpty()) {
            return DEFAULT_RECIPE;
        }

        return recipe.get().getImagePath();
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex);
    }

    public String getAvatarImageUrl(String username) {
        User user = userService.getUser(username);
        if (user.getAvatarPath() == null || user.getAvatarPath().isEmpty()) {
            return DEFAULT_AVATAR;
        }
        
        return user.getAvatarPath();
    }

    public String getRecipeImageUrl(Long recipeId) {
        Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);
        if (recipe.isEmpty()) {
            return DEFAULT_RECIPE;
        }

        return recipe.get().getImagePath();
    }
}
