package org.dvd.remixifyapi.recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient;
import org.dvd.remixifyapi.recipe.repository.RecipeRepository;
import org.dvd.remixifyapi.recommendation.dto.RecommendationDto;
import org.dvd.remixifyapi.recommendation.dto.RecommendationResponse;
import org.dvd.remixifyapi.recommendation.dto.Stats;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentBasedRecommendationService implements RecommendationService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    private final Map<Long, double[]> recipeVectors = new HashMap<>();
    private Map<String, Integer> ingredientIndexMap = new HashMap<>();
    private int ingredientVectorSize = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void initVectors() {
        ingredientIndexMap.clear();
        int index = 0;

        for (Recipe recipe : recipeRepository.findAllWithIngredients()) {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                String ingredientName = ri.getIngredient().getName().toLowerCase().trim();
                if (!ingredientIndexMap.containsKey(ingredientName)) {
                    ingredientIndexMap.put(ingredientName, index++);
                }
            }
        }

        ingredientVectorSize = ingredientIndexMap.size();
        for (Recipe recipe : recipeRepository.findAllWithIngredients()) {
            recipeVectors.put(recipe.getId(), computeVector(recipe));
        }
    }

    @Override
    @Cacheable("recommendations")
    public RecommendationResponse recommend(String username, int limit) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Set<Recipe> userLikes = user.getLikedRecipes();
        if (userLikes.isEmpty()) {
            return new RecommendationResponse(Collections.emptyList(), new Stats(0, 0, 0));
        }

        List<double[]> userVectors = userLikes.stream()
                .map(likedRecipe -> recipeVectors.get(likedRecipe.getId()))
                .filter(Objects::nonNull)
                .toList();

        double[] userProfile = averageVectors(userVectors);
        if (userProfile == null) {
            log.warn("No valid recipe vectors found for this user. Returning empty recommendations.");
            return new RecommendationResponse(Collections.emptyList(), new Stats(0, 0, 0));
        }

        List<Recipe> recipes = recipeRepository.findAll();
        List<RecommendationDto> recommendations = recipes.stream()
                .filter(recipe -> !userLikes.contains(recipe))
                .map(recipe -> {
                    double[] recipeVector = recipeVectors.get(recipe.getId());
                    double score = recipeVector != null ? cosine(userProfile, recipeVector) : 0.0;
                    return new RecommendationDto(recipe.getId(), recipe.getName(), score);
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .toList();

        Stats stats = computeStats(recommendations);
        return new RecommendationResponse(recommendations, stats);
    }

    private double[] computeVector(Recipe recipe) {
        double[] vector = new double[ingredientVectorSize]; // 1 if ingredient present, 0 if not
        try {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                String ingredientName = ri.getIngredient().getName().toLowerCase().trim();
                Integer i = ingredientIndexMap.get(ingredientName);
                if (i != null) {
                    vector[i] = 1.0;
                }
            }
        } catch (Exception e) {
            log.error("Error processing ingredients: " + e.getMessage());
        }
        normalizeVector(vector);
        return vector;
    }

    private void normalizeVector(double[] vector) {
        double magnitude = 0.0;
        for (double value : vector) {
            magnitude += value * value;
        }

        if (magnitude > 0) {
            magnitude = Math.sqrt(magnitude);
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= magnitude;
            }
        }
    }

    private double[] averageVectors(List<double[]> vectors) {
        if (vectors.isEmpty()) {
            return null;
        }
        double[] avg = new double[vectors.get(0).length];
        for (double[] v : vectors) {
            for (int i = 0; i < avg.length; i++) {
                avg[i] += v[i];
            }
        }
        for (int i = 0; i < avg.length; i++) {
            avg[i] /= vectors.size();
        }
        return avg;
    }

    private double cosine(double[] a, double[] b) {
        if (a == null || b == null) {
            return 0.0;
        }
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) {
            return 0.0;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private Stats computeStats(List<RecommendationDto> list) {
        if (list.isEmpty()) {
            return new Stats(0, 0, 0);
        }

        double max = list.stream()
                .mapToDouble(RecommendationDto::getScore)
                .max()
                .orElse(0);
        double min = list.stream()
                .mapToDouble(RecommendationDto::getScore)
                .min()
                .orElse(0);
        double avg = list.stream()
                .mapToDouble(RecommendationDto::getScore)
                .average()
                .orElse(0);

        return new Stats(max, min, avg);
    }
}
