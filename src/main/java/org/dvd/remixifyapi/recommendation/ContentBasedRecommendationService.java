package org.dvd.remixifyapi.recommendation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.transaction.annotation.Transactional;

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
    private Map<String, Integer> ingredientFrequency = new HashMap<>();
    private int ingredientVectorSize = 0;
    private int totalRecipes = 0;

    @EventListener(ApplicationReadyEvent.class)
    public void initVectors() {
        ingredientIndexMap.clear();
        ingredientFrequency.clear();
        int index = 0;

        List<Recipe> allRecipes = recipeRepository.findAllWithIngredients();
        totalRecipes = allRecipes.size();

        // Build ingredient index map
        for (Recipe recipe : allRecipes) {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                String ingredientName = ri.getIngredient().getName().toLowerCase().trim();
                if (!ingredientIndexMap.containsKey(ingredientName)) {
                    ingredientIndexMap.put(ingredientName, index++);
                }
            }
        }

        // Calculate ingredient frequencies
        for (Recipe recipe : allRecipes) {
            Set<String> uniqueIngredients = new HashSet<>();
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                String ingredientName = ri.getIngredient().getName().toLowerCase().trim();
                uniqueIngredients.add(ingredientName);
            }
            for (String ingredientName : uniqueIngredients) {
                ingredientFrequency.put(ingredientName, 
                    ingredientFrequency.getOrDefault(ingredientName, 0) + 1);
            }
        }

        ingredientVectorSize = ingredientIndexMap.size();
        for (Recipe recipe : allRecipes) {
            recipeVectors.put(recipe.getId(), computeVector(recipe)); 
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable("recommendations")
    public RecommendationResponse recommend(String username, int limit) {
        User user = userRepository.findByUsernameWithRecipes(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Set<Recipe> userLikes = user.getLikedRecipes();
        if (userLikes.isEmpty()) {
            log.info("No liked recipes found for user: {}. Cannot generate recommendations.", username);
            return new RecommendationResponse(
                Collections.emptyList(), 
                new Stats(0, 0, 0),
                "To get personalized recommendations, please like some recipes first!"
            );
        }

        List<double[]> userVectors = userLikes.stream()
                .map(likedRecipe -> recipeVectors.get(likedRecipe.getId()))
                .filter(Objects::nonNull)
                .toList();

        double[] userProfile = averageVectors(userVectors);
        if (userProfile == null) {
            log.warn("No valid recipe vectors found for user: {}. Returning empty recommendations.", username);
            return new RecommendationResponse(
                Collections.emptyList(), 
                new Stats(0, 0, 0),
                "We're having trouble analyzing your taste preferences. Please try again later."
            );
        }

        Set<String> userLikedIngredients = userLikes.stream()
                .flatMap(likedRecipe -> likedRecipe.getRecipeIngredients().stream())
                .map(ri -> ri.getIngredient().getName().toLowerCase())
                .collect(Collectors.toSet());

        List<Recipe> recipes = recipeRepository.findAllWithIngredients();
        List<RecommendationDto> recommendations = recipes.stream()
                .filter(recipe -> !userLikes.contains(recipe))
                .map(recipe -> {
                    double[] recipeVector = recipeVectors.get(recipe.getId());
                    double score = recipeVector != null
                        ? calculateCosineSimilarity(userProfile, recipeVector)
                        : 0.0;

                    List<String> commonIngredients = recipe.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName().toLowerCase())
                            .filter(userLikedIngredients::contains)
                            .collect(Collectors.toList());

                    String explanation;
                    if (!commonIngredients.isEmpty()) {
                        explanation = String.format("You might like this because you've enjoyed recipes with %s",
                                String.join(", ", commonIngredients));
                    } else if (score < 0.3) {
                        explanation = "We couldn't find a perfect match, but this recipe has some new ingredients you might like";
                    } else {
                        explanation = "Based on your taste profile, you might enjoy this recipe";
                    }

                    return new RecommendationDto(
                            recipe.getId(),
                            recipe.getName(),
                            score,
                            commonIngredients,
                            explanation);
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(limit)
                .collect(Collectors.toList());


        recommendations = recommendations.stream()
            .map(dto -> new RecommendationDto(
                    dto.getId(),
                    dto.getTitle(),
                    (int) Math.round(Math.sqrt(dto.getScore()) * 100),
                    dto.getCommonIngredients(),
                    dto.getExplanation()))
            .collect(Collectors.toList());

        Stats stats = computeStats(recommendations);
        String explanation = recommendations.isEmpty() 
            ? "No recipes match your preferences at the moment. Try liking more recipes!"
            : String.format("Found %d personalized recommendations based on your taste profile.", recommendations.size());
        return new RecommendationResponse(recommendations, stats, explanation);
    }

    private double[] computeVector(Recipe recipe) {
        double[] vector = new double[ingredientVectorSize];
        try {
            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                String ingredientName = ri.getIngredient().getName().toLowerCase().trim();
                Integer i = ingredientIndexMap.get(ingredientName);
                if (i != null) {
                    // Apply inverse frequency weighting (TF-IDF)
                    int frequency = ingredientFrequency.getOrDefault(ingredientName, 1);
                    double weight = Math.log((double) totalRecipes / frequency);
                    vector[i] = weight;
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

    private double calculateCosineSimilarity(double[] a, double[] b) {
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
