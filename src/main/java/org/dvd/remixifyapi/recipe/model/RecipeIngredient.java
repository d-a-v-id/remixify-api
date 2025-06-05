package org.dvd.remixifyapi.recipe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "recipe", "ingredient" })
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private double quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    public String getFullMeasure() {
        if (unit != null) {
            return quantity + " " + unit.getDisplayName();
        }
        return String.valueOf(quantity);
    }

    public enum Unit {
        GRAM("g"),
        KILOGRAM("kg"),
        MILLILITER("ml"),
        LITER("l"),
        TABLESPOON("tbsp"),
        PIECE("piece"),
        PINCH("pinch"),
        CUP("cup"),
        TO_TASTE("to taste"),
        NONE("none");

        private final String displayName;

        Unit(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}