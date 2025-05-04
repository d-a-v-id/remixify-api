package org.dvd.remixifyapi.recipe.dto;

import java.util.List;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.storage.util.FileStorageUtils;
import org.dvd.remixifyapi.user.model.User;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private String imagePath;
    private RecipeUserDto author;
    private String label;
    private String labelDescription;
    private List<String> steps;
    private List<IngredientDto> ingredients;
    private Long cookTime;
    private Integer servings;
    private String difficulty;
    private Long likes;
    private boolean isLikedByUser;

    @JsonCreator
    public RecipeDto(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("imagePath") String imagePath,
            @JsonProperty("author") RecipeUserDto author,
            @JsonProperty("label") String label,
            @JsonProperty("labelDescription") String labelDescription,
            @JsonProperty("steps") List<String> steps,
            @JsonProperty("ingredients") List<IngredientDto> ingredients,
            @JsonProperty("cookTime") Long cookTime,
            @JsonProperty("servings") Integer servings,
            @JsonProperty("difficulty") String difficulty,
            @JsonProperty("likes") Long likes,
            @JsonProperty("isLikedByUser") boolean isLikedByUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.author = author;
        this.label = label;
        this.labelDescription = labelDescription;
        this.steps = steps;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
        this.servings = servings;
        this.difficulty = difficulty;
        this.likes = likes;
        this.isLikedByUser = isLikedByUser;
    }

    @Data
    @Builder
    @NoArgsConstructor
    public static class IngredientDto {
        private Long id;
        private String name;
        private double quantity;
        private String unit;

        @JsonCreator
        public IngredientDto(
                @JsonProperty("id") Long id,
                @JsonProperty("name") String name,
                @JsonProperty("quantity") double quantity,
                @JsonProperty("unit") String unit) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }

    public static RecipeDto fromRecipe(Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .imagePath(FileStorageUtils.getFullRecipeImageUrl(recipe.getImagePath()))
                .author(RecipeUserDto.fromUser(recipe.getAuthor()))
                .label(recipe.getLabel() != null ? recipe.getLabel().name() : null)
                .labelDescription(recipe.getLabel() != null ? recipe.getLabel().getDescription() : null)
                .steps(recipe.getSteps())
                .ingredients(recipe.getRecipeIngredients().stream()
                        .map(ri -> IngredientDto.builder()
                                .id(ri.getIngredient().getId())
                                .name(ri.getIngredient().getName())
                                .quantity(ri.getQuantity())
                                .unit(ri.getUnit().name())
                                .build())
                        .toList())
                .cookTime(recipe.getCookTime())
                .servings(recipe.getServings())
                .difficulty(recipe.getDifficulty() != null ? recipe.getDifficulty().name() : null)
                .likes(recipe.getLikes())
                .isLikedByUser(false)
                .build();
    }

    public static RecipeDto fromRecipe(Recipe recipe, User currentUser) {
        RecipeDto dto = fromRecipe(recipe);
        if (dto != null && currentUser != null) {
            dto.setLikedByUser(recipe.getLikedByUsers().contains(currentUser));
        }
        return dto;
    }
}