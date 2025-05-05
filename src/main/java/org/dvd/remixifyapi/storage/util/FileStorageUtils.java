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

    @Value("${app.base-url}")
    public void setRemixifyBaseUrl(String url) {
        remixifyBaseUrl = url;
    }

    @Value("${aws.s3.base-url:}")
    public void setS3BaseUrl(String url) {
        s3BaseUrl = url;
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

        // If it's an S3 path and we have an S3 base URL configured
        if (avatarPath.startsWith("uploads/") && s3BaseUrl != null && !s3BaseUrl.isEmpty()) {
            return s3BaseUrl + "/" + avatarPath;
        }

        // For local storage, check if file exists
        if (!Files.exists(Path.of(avatarPath))) {
            return getDefaultAvatarUrl();
        }

        return prependBaseUrl(avatarPath);
    }

    public static String getFullRecipeImageUrl(String recipeImagePath) {
        if (recipeImagePath == null || recipeImagePath.isEmpty()) {
            return getDefaultRecipeUrl();
        }

        // If it's already a full URL (http or https), return as is
        if (recipeImagePath.startsWith("http")) {
            return recipeImagePath;
        }

        // If it's an S3 path and we have an S3 base URL configured
        if (recipeImagePath.startsWith("uploads/") && s3BaseUrl != null && !s3BaseUrl.isEmpty()) {
            return s3BaseUrl + "/" + recipeImagePath;
        }

        // For local storage, check if file exists
        if (!Files.exists(Path.of(recipeImagePath))) {
            return getDefaultRecipeUrl();
        }

        return prependBaseUrl(recipeImagePath);
    }

    private static String getDefaultAvatarUrl() {
        if (s3BaseUrl != null && !s3BaseUrl.isEmpty()) {
            return s3BaseUrl + "/" + DEFAULT_AVATAR_PATH;
        }
        return prependBaseUrl(DEFAULT_AVATAR_PATH);
    }

    private static String getDefaultRecipeUrl() {
        if (s3BaseUrl != null && !s3BaseUrl.isEmpty()) {
            return s3BaseUrl + "/" + DEFAULT_RECIPE_PATH;
        }
        return prependBaseUrl(DEFAULT_RECIPE_PATH);
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
