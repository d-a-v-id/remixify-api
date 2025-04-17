package org.dvd.remixifyapi.storage.util;

import java.nio.file.Files;
import java.nio.file.Path;

public interface FileStorageConstants {
    String RECIPES_PATH = "uploads/recipes";
    String AVATARS_PATH = "uploads/avatars";
    String DEFAULT_AVATAR_PATH = "uploads/avatars/default-avatar.webp";
    String DEFAULT_RECIPE_PATH = "uploads/recipes/default-recipe.webp";


    public static String getAvatarUrl(String username) {
        String path = AVATARS_PATH + "/" + username + ".jpg";
        if (!Files.exists(Path.of(path))) {
            return DEFAULT_AVATAR_PATH;
        }
        return AVATARS_PATH + "/" + username + ".jpg";
    }

    public static String getRecipeUrl(String recipeId) {
        String path = RECIPES_PATH + "/" + recipeId + ".jpg";
        if (!Files.exists(Path.of(path))) {
            return DEFAULT_RECIPE_PATH;
        }
        return RECIPES_PATH + "/" + recipeId + ".jpg";
    }
}
