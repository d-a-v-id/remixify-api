package org.dvd.remixifyapi.recipe.repository;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT DISTINCT r FROM Recipe r " +
           "LEFT JOIN FETCH r.recipeIngredients ri " +
           "LEFT JOIN FETCH ri.ingredient " +
           "LEFT JOIN FETCH r.likedByUsers " +
           "WHERE r.id = :id")
    Optional<Recipe> findByIdWithRelationships(@Param("id") Long id);

    Page<Recipe> findByLabel(Label label, Pageable pageable);

    Page<Recipe> findByOrderByLikesDesc(Pageable pageable);

    Page<Recipe> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Recipe> findByName(String name);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.recipeIngredients ri JOIN ri.ingredient i WHERE LOWER(i.name) IN :ingredients")
    Page<Recipe> findByRecipeIngredients(@Param("ingredients") List<String> ingredients, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.author a WHERE LOWER(a.username) = LOWER(:author)")
    Page<Recipe> findByAuthor(String author, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Recipe r " +
           "LEFT JOIN r.author a " +
           "WHERE (:author IS NULL OR LOWER(a.username) = LOWER(:author)) " +
           "AND (:label IS NULL OR r.label = :label)")
    Page<Recipe> findByAuthorAndLabel(@Param("author") String author, @Param("label") Label label, Pageable pageable);
}
