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

        Recipe freshRecipe = recipeRepository.findByIdWithRelationships(recipe.getId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!freshRecipe.getLikedByUsers().contains(freshUser)) {
            freshRecipe.getLikedByUsers().add(freshUser);
            freshUser.getLikedRecipes().add(freshRecipe);
            freshRecipe.setLikes((long) freshRecipe.getLikedByUsers().size());
            
            recipeRepository.save(freshRecipe);
            userRepository.save(freshUser);
        }
        return freshRecipe;
    }

    @Transactional
    public Recipe unlikeRecipe(Recipe recipe, User user) {
        User freshUser =
                userRepository
                        .findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe freshRecipe = recipeRepository.findById(recipe.getId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (freshRecipe.getLikedByUsers().contains(freshUser)) {
            freshRecipe.getLikedByUsers().remove(freshUser);
            freshUser.getLikedRecipes().remove(freshRecipe);
            freshRecipe.setLikes((long) freshRecipe.getLikedByUsers().size());
            
            recipeRepository.save(freshRecipe);
            userRepository.save(freshUser);
        }
        return freshRecipe;
    }

    @Transactional(readOnly = true)
    public List<Recipe> getLikedRecipesByUser(User user) {
        return recipeRepository.findRecipesLikedByUserId(user.getId());
    }
}
