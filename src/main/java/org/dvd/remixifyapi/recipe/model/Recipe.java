package org.dvd.remixifyapi.recipe.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dvd.remixifyapi.recipe.model.RecipeIngredient.Unit;
import org.dvd.remixifyapi.user.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {

    // ─────────────────────────────────────────────────────
    // 📌 Basic Fields
    // ─────────────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private String imagePath;

    private List<String> steps;

    private Long cookTime;

    private int servings;

    @Builder.Default private Long likes = 0L;

    // ─────────────────────────────────────────────────────
    // 📌 Relationships
    // ─────────────────────────────────────────────────────

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "Author is required")
    private User author;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

    @ManyToMany(mappedBy = "likedRecipes")
    @Builder.Default
    private Set<User> likedByUsers = new HashSet<>();

    // ─────────────────────────────────────────────────────
    // 📌 Enums
    // ─────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Label label;

    // ─────────────────────────────────────────────────────
    // 📌 Business Logic
    // ─────────────────────────────────────────────────────

    public void like(User user) {
        this.likedByUsers.add(user);
        user.getLikedRecipes().add(this);
        this.likes = (long) this.likedByUsers.size();
    }

    public void unlike(User user) {
        this.likedByUsers.remove(user);
        user.getLikedRecipes().remove(this);
        this.likes = (long) this.likedByUsers.size();
    }

    public boolean isLikedBy(User user) {
        return likedByUsers.contains(user);
    }

    public void addRecipeIngredient(Ingredient ingredient, String quantity, Unit unit) {
        RecipeIngredient recipeIngredient =
                RecipeIngredient.builder().recipe(this).ingredient(ingredient).unit(unit).build();
        recipeIngredients.add(recipeIngredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        recipeIngredients.removeIf(e -> e.getIngredient().equals(ingredient));
    }

    // ─────────────────────────────────────────────────────
    // 📌 Enums Definitions
    // ─────────────────────────────────────────────────────

    public enum Label {
        QUICK,
        VEGAN,
        TRENDING,
        HEALTHY,
        DESSERT;

        public String getDescription() {
            return switch (this) {
                case TRENDING -> "🔥 Hot recipe";
                case VEGAN -> "🌱 Vegan";
                case QUICK -> "⌛ Quick meal";
                case HEALTHY -> "💪 Healthy choice";
                case DESSERT -> "🤤 Sweet treat";
            };
        }
    }

    public enum Difficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard");

        private final String displayName;

        Difficulty(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
