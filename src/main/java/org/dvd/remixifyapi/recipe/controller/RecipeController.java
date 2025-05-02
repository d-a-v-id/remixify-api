package org.dvd.remixifyapi.recipe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dvd.remixifyapi.recipe.dto.RecipeDto;
import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.Recipe.Label;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient;
import org.dvd.remixifyapi.recipe.repository.IngredientRepository;
import org.dvd.remixifyapi.recipe.service.RecipeLikeService;
import org.dvd.remixifyapi.recipe.service.RecipeService;
import org.dvd.remixifyapi.storage.service.FileStorageService;
import org.dvd.remixifyapi.storage.util.FileStorageUtils;
import org.dvd.remixifyapi.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;
    private final FileStorageService fileStorageService;
    private final IngredientRepository ingredientRepository;
    private final RecipeLikeService recipeLikeService;

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes(
            @AuthenticationPrincipal User currentUser) {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(
                recipes.stream()
                        .map(recipe -> RecipeDto.fromRecipe(recipe, currentUser))
                        .toList());
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<String>> getIngredients() {
        List<String> ingredients = recipeService.getAllIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(
            @PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = recipeOpt.get();
        return ResponseEntity.ok(RecipeDto.fromRecipe(recipe, currentUser));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> updateRecipe(
            @PathVariable Long recipeId,
            @Valid @RequestBody Recipe recipe,
            @AuthenticationPrincipal User currentUser) {

        Optional<Recipe> recipeOptional = recipeService.getRecipeById(recipeId);

        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recipe existingRecipe = recipeOptional.get();
        if (!existingRecipe.getAuthor().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingRecipe.setName(recipe.getName().trim());
        existingRecipe.setDescription(recipe.getDescription().trim());
        Recipe updatedRecipe = recipeService.updateRecipe(existingRecipe);
        return ResponseEntity.ok(RecipeDto.fromRecipe(updatedRecipe, currentUser));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Long recipeId, @AuthenticationPrincipal User currentUser) {

        Optional<Recipe> recipeOptional = recipeService.getRecipeById(recipeId);

        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Recipe recipe = recipeOptional.get();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!recipe.getAuthor().getId().equals(currentUser.getId()) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        recipeService.deleteRecipe(recipe);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecipeDto> createRecipe(
            @RequestPart("recipe") String recipeJson,
            @AuthenticationPrincipal User currentUser,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RecipeDto recipeRequest = objectMapper.readValue(recipeJson, RecipeDto.class);

            Recipe recipe =
                    Recipe.builder()
                            .name(recipeRequest.getName())
                            .description(recipeRequest.getDescription())
                            .author(currentUser)
                            .steps(recipeRequest.getSteps())
                            .label(
                                    recipeRequest.getLabel() != null
                                            ? Recipe.Label.valueOf(recipeRequest.getLabel())
                                            : null)
                            .cookTime(recipeRequest.getCookTime())
                            .servings(recipeRequest.getServings())
                            .difficulty(Recipe.Difficulty.valueOf(recipeRequest.getDifficulty()))
                            .likes(0L)
                            .build();

            List<RecipeIngredient> ingredients = new ArrayList<>();
            for (RecipeDto.IngredientDto ingredientDto : recipeRequest.getIngredients()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipe(recipe);

                Optional<Ingredient> ingredientOpt =
                        ingredientRepository.findById(ingredientDto.getId());
                if (ingredientOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body(null);
                }

                ri.setIngredient(ingredientOpt.get());
                ri.setQuantity(ingredientDto.getQuantity());
                ri.setUnit(RecipeIngredient.Unit.valueOf(ingredientDto.getUnit().toUpperCase()));
                ingredients.add(ri);
            }
            recipe.setRecipeIngredients(ingredients);

            if (image != null && !image.isEmpty()) {
                String imagePath =
                        fileStorageService.storeFile(image, FileStorageUtils.RECIPES_PATH);
                recipe.setImagePath(imagePath);
            }

            Recipe savedRecipe = recipeService.saveRecipe(recipe);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(RecipeDto.fromRecipe(savedRecipe, currentUser));
        } catch (Exception e) {
            log.error("Error creating recipe: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/likes")
    public ResponseEntity<List<RecipeDto>> getLikedRecipes(
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Recipe> likedRecipes = recipeLikeService.getLikedRecipesByUser(currentUser);

        if (likedRecipes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<RecipeDto> likedRecipesDto =
                likedRecipes.stream()
                        .map(recipe -> RecipeDto.fromRecipe(recipe, currentUser))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(likedRecipesDto);
    }

    @GetMapping("/category/{label}")
    public ResponseEntity<List<RecipeDto>> getRecipesByLabel(
            @PathVariable String label,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Label recipeLabel = Label.valueOf(label.toUpperCase());
            List<Recipe> recipes = recipeService.getRecipesByLabel(recipeLabel, limit);
            
            List<RecipeDto> recipeDtos = recipes.stream()
                    .map(recipe -> RecipeDto.fromRecipe(recipe))
                    .toList();
            
            return ResponseEntity.ok(recipeDtos);
        } catch (IllegalArgumentException e) {
            log.error("Invalid recipe label: {}", label);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/most-liked")
    public ResponseEntity<List<RecipeDto>> getMostLikedRecipes(
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal User currentUser) {
        List<Recipe> recipes = recipeService.getMostLikedRecipes(limit);
        
        List<RecipeDto> recipeDtos = recipes.stream()
                .map(recipe -> RecipeDto.fromRecipe(recipe, currentUser))
                .toList();
        
        return ResponseEntity.ok(recipeDtos);
    }
}
