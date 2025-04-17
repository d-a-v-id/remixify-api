package org.dvd.remixifyapi.recipe.service;

import java.util.List;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.repository.RecipeRepository;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeLikeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Recipe likeRecipe(Recipe recipe, User user) {
        User freshUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        if (!recipe.getLikedByUsers().contains(freshUser)) {
            recipe.getLikedByUsers().add(freshUser);
            freshUser.getLikedRecipes().add(recipe);
            recipe.setLikes((long) recipe.getLikedByUsers().size());
        }
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe unlikeRecipe(Recipe recipe, User user) {
        User freshUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        if (recipe.getLikedByUsers().contains(freshUser)) {
            recipe.getLikedByUsers().remove(freshUser);
            freshUser.getLikedRecipes().remove(recipe);
            recipe.setLikes((long) recipe.getLikedByUsers().size());
        }
        return recipeRepository.save(recipe);
    }

    @Transactional(readOnly = true)
    public List<Recipe> getLikedRecipesByUser(User user) {
        return recipeRepository.findRecipesLikedByUserId(user.getId());
    }
}
