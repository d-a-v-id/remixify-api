package org.dvd.remixifyapi.recipe.repository;

import java.util.Optional;

import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
}
