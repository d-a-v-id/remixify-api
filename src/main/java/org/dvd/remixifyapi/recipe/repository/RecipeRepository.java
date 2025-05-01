package org.dvd.remixifyapi.recipe.repository;

import java.util.List;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.Recipe.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN FETCH r.recipeIngredients ri LEFT JOIN FETCH ri.ingredient")
    List<Recipe> findAllWithIngredients();
    
    @Query("SELECT r FROM Recipe r JOIN FETCH r.likedByUsers u WHERE u.id = :userId")
    List<Recipe> findRecipesLikedByUserId(@Param("userId") Long userId);

    Page<Recipe> findByLabel(Label label, Pageable pageable);

    Page<Recipe> findByOrderByLikesDesc(Pageable pageable);
}
