package org.dvd.remixifyapi.recipe.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient.Unit;
import org.dvd.remixifyapi.user.dto.UserDto;
import org.dvd.remixifyapi.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private String imagePath;
    private UserDto author;
    private List<IngredientDto> ingredients;
    private List<String> steps;
    private String label;
    private String labelDescription;
    private Long likes;
    private boolean isLikedByUser;
    private Long cookTime;
    private int servings;
    private String difficulty;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDto {
        private Long id;
        private String name;
        private Double quantity;
        private String unit;
    }

    public static RecipeDto fromRecipe(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .imagePath(recipe.getImagePath())
                .author(UserDto.fromUser(recipe.getAuthor()))
                .ingredients(recipe.getRecipeIngredients().stream()
                        .map(e -> IngredientDto.builder()
                                .id(e.getIngredient().getId())
                                .name(e.getIngredient().getName())
                                .quantity(e.getQuantity())
                                .unit(e.getUnit().getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .steps(recipe.getSteps())
                .label(recipe.getLabel() != null ? recipe.getLabel().name() : null)
                .labelDescription(recipe.getLabel() != null ? recipe.getLabel().getDescription() : null)
                .likes(recipe.getLikes())
                .cookTime(recipe.getCookTime())
                .servings(recipe.getServings())
                .difficulty(recipe.getDifficulty().getDisplayName())
                .build();
    }

    public static RecipeDto fromRecipe(Recipe recipe, User currentUser) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .imagePath(recipe.getImagePath())
                .author(UserDto.fromUser(recipe.getAuthor()))
                .ingredients(recipe.getRecipeIngredients().stream()
                        .map(e -> IngredientDto.builder()
                                .id(e.getIngredient().getId())
                                .name(e.getIngredient().getName())
                                .quantity(e.getQuantity())
                                .unit(e.getUnit().getDisplayName())
                                .build())
                        .collect(Collectors.toList()))
                .steps(recipe.getSteps())
                .label(recipe.getLabel() != null ? recipe.getLabel().name() : null)
                .labelDescription(recipe.getLabel() != null ? recipe.getLabel().getDescription() : null)
                .likes(recipe.getLikes())
                .isLikedByUser(recipe.isLikedBy(currentUser))
                .cookTime(recipe.getCookTime())
                .servings(recipe.getServings())
                .difficulty(recipe.getDifficulty().getDisplayName())
                .build();
    }

    public static Recipe toRecipe(RecipeDto recipeRequest) {
        return Recipe.builder()
                .name(recipeRequest.getName())
                .steps(recipeRequest.getSteps())
                .label(Recipe.Label.valueOf(recipeRequest.getLabel()))
                .recipeIngredients(recipeRequest.getIngredients().stream()
                        .map(e -> RecipeIngredient.builder()
                                .ingredient(Ingredient.builder().id(e.getId()).build())
                                .quantity(e.getQuantity())
                                .unit(Unit.valueOf(e.getUnit()))
                                .build())
                        .toList())
                .description(recipeRequest.getDescription())
                .imagePath(recipeRequest.getImagePath())
                .author(User.builder().id(recipeRequest.getAuthor().getId()).build())
                .likes(0L)
                .cookTime(recipeRequest.getCookTime())
                .servings(recipeRequest.getServings())
                .difficulty(Recipe.Difficulty.valueOf(recipeRequest.getDifficulty()))
                .build();
    }
}