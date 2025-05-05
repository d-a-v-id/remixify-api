package org.dvd.remixifyapi.storage.util;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class FileStorageUtils {
    @Getter
    private static String remixifyBaseUrl;
    private static String s3BaseUrl;
    private static String storageType;

    @Value("${app.base-url}")
    public void setRemixifyBaseUrl(String url) {
        remixifyBaseUrl = url;
    }

    @Value("${aws.s3.base-url:}")
    public void setS3BaseUrl(String url) {
        s3BaseUrl = url;
    }

    @Value("${app.storage.type:local}")
    public void setStorageType(String type) {
        storageType = type;
    }

    public static final String RECIPES_PATH = "uploads/recipes";
    public static final String AVATARS_PATH = "uploads/avatars";
    public static final String DEFAULT_AVATAR_PATH = "uploads/avatars/default-avatar.webp";
    public static final String DEFAULT_RECIPE_PATH = "uploads/recipes/default-recipe.webp";

    public static String getFullAvatarImageUrl(String avatarPath) {
        if (avatarPath == null || avatarPath.isEmpty()) {
            return getDefaultAvatarUrl();
        }

        // If it's already a full URL (http or https), return as is
        if (avatarPath.startsWith("http")) {
            return avatarPath;
        }

        return getFullUrl(avatarPath);
    }

    public static String getFullRecipeImageUrl(String recipeImagePath) {
        if (recipeImagePath == null || recipeImagePath.isEmpty()) {
            return getDefaultRecipeUrl();
        }

        // If it's already a full URL (http or https), return as is
        if (recipeImagePath.startsWith("http")) {
            return recipeImagePath;
        }

        return getFullUrl(recipeImagePath);
    }

    private static String getDefaultAvatarUrl() {
        return getFullUrl(DEFAULT_AVATAR_PATH);
    }

    private static String getDefaultRecipeUrl() {
        return getFullUrl(DEFAULT_RECIPE_PATH);
    }

    private static String getFullUrl(String path) {
        if (path == null) {
            return null;
        }

        // For S3 storage
        if ("s3".equalsIgnoreCase(storageType) && s3BaseUrl != null && !s3BaseUrl.isEmpty()) {
            return s3BaseUrl + "/" + path;
        }

        // For local storage
        if (!Files.exists(Path.of(path))) {
            return null;
        }

        return prependBaseUrl(path);
    }

    private static String prependBaseUrl(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("http")) {
            return path;
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return remixifyBaseUrl + path;
    }
}
