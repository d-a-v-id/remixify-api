package org.dvd.remixifyapi.recipe.service;

import java.util.List;
import java.util.Optional;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.Recipe.Label;
import org.dvd.remixifyapi.recipe.repository.IngredientRepository;
import org.dvd.remixifyapi.recipe.repository.RecipeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(Recipe recipe) {
        recipe.getLikedByUsers().forEach(user -> {
            user.getLikedRecipes().remove(recipe);
        });
        recipe.getLikedByUsers().clear();
        
        recipe.getRecipeIngredients().clear();
        
        recipeRepository.save(recipe);
        
        recipeRepository.delete(recipe);
    }

    public List<String> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(ingredient -> ingredient.getName())
                .toList();
    }

    public List<Recipe> getRecipesByLabel(Label label, int limit) {
        return recipeRepository.findByLabel(label, PageRequest.of(0, limit)).getContent();
    }

    public List<Recipe> getMostLikedRecipes(int limit) {
        return recipeRepository.findByOrderByLikesDesc(PageRequest.of(0, limit)).getContent();
    }
}
