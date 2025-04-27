package org.dvd.remixifyapi.recipe.controller;

import java.util.Optional;

import org.dvd.remixifyapi.recipe.dto.RecipeDto;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.user.model.User;


import org.dvd.remixifyapi.recipe.service.RecipeService;

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
    private final org.dvd.remixifyapi.user.repository.UserRepository userRepository;

    @PostMapping("/{recipeId}/likes")
    public ResponseEntity<RecipeDto> likeRecipe(
            @PathVariable Long recipeId, @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isEmpty()) {
            log.warn("Recipe with id {} not found", recipeId);
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = recipeOpt.get();
        currentUser.getLikedRecipes().add(recipe);
        userRepository.save(currentUser);
        return ResponseEntity.ok(RecipeDto.fromRecipe(recipe, currentUser));
    }

    @DeleteMapping("/{recipeId}/likes")
    public ResponseEntity<Void> unlikeRecipe(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Optional<User> userOpt = userRepository.findByUsername(principal.getUsername());
        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User currentUser = userOpt.get();

        Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
        if (recipeOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Recipe recipe = recipeOpt.get();
        currentUser.getLikedRecipes().remove(recipe);
        userRepository.save(currentUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
