package org.dvd.remixifyapi.recipe.controller;

import java.util.Optional;

import org.dvd.remixifyapi.recipe.dto.RecipeDto;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.service.RecipeLikeService;
import org.dvd.remixifyapi.recipe.service.RecipeService;
import org.dvd.remixifyapi.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeLikeController {

    private final RecipeService recipeService;
    private final RecipeLikeService recipeLikeService;

    @PostMapping("/{recipeId}/likes")
    public ResponseEntity<RecipeDto> likeRecipe(
            @PathVariable Long recipeId, @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            log.warn("Unauthorized attempt to like recipe");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isEmpty()) {
            log.warn("Recipe with id {} not found", recipeId);
            return ResponseEntity.notFound().build();
        }

        try {
            Recipe recipe = recipeOpt.get();
            Recipe updatedRecipe = recipeLikeService.likeRecipe(recipe, currentUser);
            return ResponseEntity.ok(RecipeDto.fromRecipe(updatedRecipe, currentUser));
        } catch (Exception e) {
            log.error("Error liking recipe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{recipeId}/likes")
    public ResponseEntity<Void> unlikeRecipe(
            @PathVariable Long recipeId, @AuthenticationPrincipal User currentUser) {
        
        if (currentUser == null) {
            log.warn("Unauthorized attempt to unlike recipe");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isEmpty()) {
            log.warn("Recipe with id {} not found", recipeId);
            return ResponseEntity.notFound().build();
        }

        try {
            Recipe recipe = recipeOpt.get();
            recipeLikeService.unlikeRecipe(recipe, currentUser);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error unliking recipe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
