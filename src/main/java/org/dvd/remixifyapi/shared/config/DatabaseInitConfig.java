package org.dvd.remixifyapi.shared.config;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient;
import org.dvd.remixifyapi.recipe.repository.IngredientRepository;
import org.dvd.remixifyapi.recipe.repository.RecipeRepository;
import org.dvd.remixifyapi.recipe.service.RecipeLikeService;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile({ "dev", "pro" })
@RequiredArgsConstructor
public class DatabaseInitConfig {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeLikeService recipeLikeService;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            // Check if we're in production
            boolean isProd = Arrays.asList(environment.getActiveProfiles()).contains("pro");

            // Initialize only if database is empty or we're in dev mode
            if (isProd && userRepository.count() > 0) {
                log.info("Database already initialized in production mode. Skipping initialization.");
                return;
            }

            log.info("Starting database initialization...");

            // Create users with existence checks
            User mama = createUserIfNotExists("mama", "Silvia", "García", "silvia@gmail.com", User.Role.USER);
            User midudev = createUserIfNotExists("midudev", "Miguel A.", "Durán", "midudev@mail.com", User.Role.USER);
            User david = createUserIfNotExists("david", "David", "Santos", "dqvid01@gmail.com", User.Role.USER);
            User admin = createUserIfNotExists("admin", "Admin", "", "admin@mail.com", User.Role.ADMIN);

            // Create ingredients with existence checks
            Ingredient avocado = createIngredientIfNotExists("Avocado");
            Ingredient bread = createIngredientIfNotExists("Whole Grain Bread");
            Ingredient eggs = createIngredientIfNotExists("Eggs");
            Ingredient salt = createIngredientIfNotExists("Salt");
            Ingredient pepper = createIngredientIfNotExists("Black Pepper");
            Ingredient oliveOil = createIngredientIfNotExists("Olive Oil");
            Ingredient lemon = createIngredientIfNotExists("Lemon");
            Ingredient chickpeas = createIngredientIfNotExists("Chickpeas");
            Ingredient tahini = createIngredientIfNotExists("Tahini");
            Ingredient garlic = createIngredientIfNotExists("Garlic");
            Ingredient cannellini = createIngredientIfNotExists("Cannellini Beans");
            Ingredient rosemary = createIngredientIfNotExists("Rosemary");
            Ingredient flour = createIngredientIfNotExists("All-Purpose Flour");
            Ingredient bakingPowder = createIngredientIfNotExists("Baking Powder");
            Ingredient sugar = createIngredientIfNotExists("Sugar");
            Ingredient butter = createIngredientIfNotExists("Butter");
            Ingredient mapleSyrup = createIngredientIfNotExists("Maple Syrup");
            Ingredient mango = createIngredientIfNotExists("Mango");
            Ingredient banana = createIngredientIfNotExists("Banana");
            Ingredient coconutMilk = createIngredientIfNotExists("Coconut Milk");
            Ingredient honey = createIngredientIfNotExists("Honey");
            Ingredient granola = createIngredientIfNotExists("Granola");
            Ingredient blueberries = createIngredientIfNotExists("Blueberries");
            Ingredient strawberries = createIngredientIfNotExists("Strawberries");
            Ingredient yogurt = createIngredientIfNotExists("Greek Yogurt");
            Ingredient milk = createIngredientIfNotExists("Milk");
            Ingredient vanilla = createIngredientIfNotExists("Vanilla Extract");
            Ingredient cinnamon = createIngredientIfNotExists("Cinnamon");
            Ingredient oats = createIngredientIfNotExists("Rolled Oats");
            Ingredient almonds = createIngredientIfNotExists("Almonds");
            Ingredient peanutButter = createIngredientIfNotExists("Peanut Butter");

            // Create recipes only if they don't exist
            Recipe avocadoToast = createRecipeIfNotExists("Avocado Toast", mama);
            Recipe avocadoEggToast = createRecipeIfNotExists("Avocado Egg Toast", mama);
            Recipe hummus = createRecipeIfNotExists("Floral Green Hummus", mama);
            Recipe whiteBeanDip = createRecipeIfNotExists("Rosemary White Bean Dip", midudev);
            Recipe pancakes = createRecipeIfNotExists("Fluffy Pancakes", mama);
            Recipe bananaPancakes = createRecipeIfNotExists("Banana Oat Pancakes", david);
            Recipe smoothieBowl = createRecipeIfNotExists("Tropical Smoothie Bowl", mama);
            Recipe berrySmoothieBowl = createRecipeIfNotExists("Berry Blast Smoothie Bowl", david);
            Recipe overnightOats = createRecipeIfNotExists("Banana Honey Overnight Oats", david);
            Recipe mangoAvocadoSalad = createRecipeIfNotExists("Mango Avocado Salad", david);
            Recipe chickpeaSalad = createRecipeIfNotExists("Mediterranean Chickpea Salad", midudev);
            Recipe parfait = createRecipeIfNotExists("Tropical Yogurt Parfait", david);
            Recipe berryParfait = createRecipeIfNotExists("Berry Yogurt Parfait", mama);
            Recipe frenchToast = createRecipeIfNotExists("French Toast", mama);
            Recipe pbBananaToast = createRecipeIfNotExists("Peanut Butter Banana Toast", midudev);

            // Set recipe ingredients only if they haven't been set before
            if (avocadoToast.getRecipeIngredients().isEmpty()) {
                avocadoToast.setRecipeIngredients(List.of(
                        createRecipeIngredient(avocadoToast, avocado, 1, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(avocadoToast, bread, 2, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(avocadoToast, salt, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(avocadoToast, pepper, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(avocadoToast, oliveOil, 1, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(avocadoToast, lemon, 1, RecipeIngredient.Unit.PIECE)));
                recipeRepository.save(avocadoToast);
            }

            // ... (similar pattern for other recipes)

            // Only set likes in dev mode or if they don't exist
            if (!isProd) {
                setRecipeLikes(mama, midudev, david, avocadoToast, hummus, smoothieBowl, mangoAvocadoSalad,
                        frenchToast);
            }

            log.info("Database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during database initialization: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private User createUserIfNotExists(String username, String firstName, String lastName, String email,
            User.Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User user = User.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .username(username)
                            .email(email)
                            .password(passwordEncoder.encode("password"))
                            .role(role)
                            .build();
                    return userRepository.save(user);
                });
    }

    private Ingredient createIngredientIfNotExists(String name) {
        return ingredientRepository.findByName(name)
                .orElseGet(() -> {
                    Ingredient ingredient = Ingredient.builder().name(name).build();
                    return ingredientRepository.save(ingredient);
                });
    }

    private Recipe createRecipeIfNotExists(String name, User author) {
        return recipeRepository.findByName(name)
                .orElseGet(() -> {
                    Recipe recipe = Recipe.builder()
                            .name(name)
                            .author(author)
                            .build();
                    return recipeRepository.save(recipe);
                });
    }

    private void setRecipeLikes(User mama, User midudev, User david, Recipe... recipes) {
        for (Recipe recipe : recipes) {
            try {
                recipeLikeService.likeRecipe(recipe, mama);
                recipeLikeService.likeRecipe(recipe, midudev);
                recipeLikeService.likeRecipe(recipe, david);
            } catch (Exception e) {
                log.warn("Failed to set like for recipe {}: {}", recipe.getName(), e.getMessage());
            }
        }
    }

    private RecipeIngredient createRecipeIngredient(
            Recipe recipe, Ingredient ingredient, int quantity, RecipeIngredient.Unit unit) {
        return RecipeIngredient.builder()
                .ingredient(ingredient)
                .quantity(quantity)
                .unit(unit)
                .recipe(recipe)
                .build();
    }
}