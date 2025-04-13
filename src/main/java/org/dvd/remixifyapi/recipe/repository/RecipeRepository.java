package org.dvd.remixifyapi.recipe.repository;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
