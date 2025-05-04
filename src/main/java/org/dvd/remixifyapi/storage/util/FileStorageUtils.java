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

    @Value("${app.base-url}")
    public void setRemixifyBaseUrl(String url) {
        remixifyBaseUrl = url;
    }

    public static final String RECIPES_PATH = "uploads/recipes";
    public static final String AVATARS_PATH = "uploads/avatars";
    public static final String DEFAULT_AVATAR_PATH = "uploads/avatars/default-avatar.webp";
    public static final String DEFAULT_RECIPE_PATH = "uploads/recipes/default-recipe.webp";

    public static String getFullAvatarImageUrl(String avatarPath) {
        if (!Files.exists(Path.of(avatarPath))) {
            return prependBaseUrl(DEFAULT_AVATAR_PATH);
        }
        return prependBaseUrl(avatarPath);
    }

    public static String getFullRecipeImageUrl(String recipeImagePath) {
        if (!Files.exists(Path.of(recipeImagePath))) {
            return prependBaseUrl(DEFAULT_RECIPE_PATH);
        }
        return prependBaseUrl(recipeImagePath);
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
