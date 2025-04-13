package org.dvd.remixifyapi.shared.config;

import java.util.Arrays;
import java.util.List;

import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.recipe.model.RecipeIngredient;
import org.dvd.remixifyapi.recipe.repository.IngredientRepository;
import org.dvd.remixifyapi.recipe.repository.RecipeRepository;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseInitConfig {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        // Create users
        User david =
                User.builder()
                        .firstName("David")
                        .lastName("Santos")
                        .username("david")
                        .email("dqvid01@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .build();
        userRepository.save(david);

        User midudev =
                User.builder()
                        .firstName("Miguel Ángel")
                        .lastName("Durán")
                        .username("midudev")
                        .email("midudev@mail.com")
                        .password(passwordEncoder.encode("password"))
                        .build();
        userRepository.save(midudev);

        User amaia =
                User.builder()
                        .firstName("Amaia")
                        .lastName("Romero")
                        .username("amaia")
                        .email("amaia@mail.com")
                        .password(passwordEncoder.encode("password"))
                        .build();
        userRepository.save(amaia);

        User admin =
                User.builder()
                        .firstName("Admin")
                        .lastName("")
                        .username("admin")
                        .email("admin@mail.com")
                        .password(passwordEncoder.encode("password"))
                        .role(User.Role.ADMIN)
                        .build();
        userRepository.save(admin);

        // Create ingredients
        Ingredient avocado = createIngredient("Avocado");
        Ingredient bread = createIngredient("Whole Grain Bread");
        Ingredient eggs = createIngredient("Eggs");
        Ingredient salt = createIngredient("Salt");
        Ingredient pepper = createIngredient("Black Pepper");
        Ingredient oliveOil = createIngredient("Olive Oil");
        Ingredient lemon = createIngredient("Lemon");

        Ingredient chickpeas = createIngredient("Chickpeas");
        Ingredient tahini = createIngredient("Tahini");
        Ingredient garlic = createIngredient("Garlic");

        Ingredient flour = createIngredient("All-Purpose Flour");
        Ingredient bakingPowder = createIngredient("Baking Powder");
        Ingredient sugar = createIngredient("Sugar");
        Ingredient butter = createIngredient("Butter");
        Ingredient mapleSyrup = createIngredient("Maple Syrup");

        Ingredient mango = createIngredient("Mango");
        Ingredient banana = createIngredient("Banana");
        Ingredient coconutMilk = createIngredient("Coconut Milk");
        Ingredient honey = createIngredient("Honey");
        Ingredient granola = createIngredient("Granola");

        Ingredient milk = createIngredient("Milk");
        Ingredient vanilla = createIngredient("Vanilla Extract");
        Ingredient cinnamon = createIngredient("Cinnamon");

        // Create recipes with ingredients and steps
        Recipe avocadoToast =
                Recipe.builder()
                        .name("Avocado Toast")
                        .description(
                                "A simple and delicious breakfast option with creamy avocado on"
                                        + " toasted bread.")
                        .imagePath("uploads/recipes/avocado-toast.jpg")
                        .author(midudev)
                        .label(Recipe.Label.HEALTHY)
                        .steps(
                                Arrays.asList(
                                        "Toast the bread slices until golden and crispy",
                                        "Mash the avocado with a fork and mix with lemon juice,"
                                                + " salt, and pepper",
                                        "Spread the mashed avocado on the toast",
                                        "Drizzle with olive oil and sprinkle with salt and pepper"))
                        .cookTime(10L)
                        .servings(2)
                        .difficulty(Recipe.Difficulty.EASY)
                        .build();
        avocadoToast = recipeRepository.save(avocadoToast);
        avocadoToast.setRecipeIngredients(
                List.of(
                        createRecipeIngredient(
                                avocadoToast, avocado, 1, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(avocadoToast, bread, 2, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(avocadoToast, salt, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(
                                avocadoToast, pepper, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(
                                avocadoToast, oliveOil, 1, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(
                                avocadoToast, lemon, 1, RecipeIngredient.Unit.PIECE)));
        recipeRepository.save(avocadoToast);

        Recipe hummus =
                Recipe.builder()
                        .name("Floral Green Hummus")
                        .description(
                                "A creamy and delicious hummus made with chickpeas, tahini, and"
                                        + " garlic.")
                        .imagePath("uploads/recipes/floral-hummus.jpg")
                        .author(david)
                        .label(Recipe.Label.VEGAN)
                        .steps(
                                Arrays.asList(
                                        "Drain and rinse the chickpeas",
                                        "In a food processor, combine chickpeas, tahini, minced"
                                                + " garlic, and lemon juice",
                                        "Add salt and olive oil",
                                        "Blend until smooth, adding water if needed for desired"
                                                + " consistency",
                                        "Taste and adjust seasonings as needed",
                                        "Transfer to a serving bowl and drizzle with olive oil"))
                        .cookTime(15L)
                        .servings(6)
                        .difficulty(Recipe.Difficulty.EASY)
                        .build();
        hummus = recipeRepository.save(hummus);
        hummus.setRecipeIngredients(
                List.of(
                        createRecipeIngredient(hummus, chickpeas, 5, RecipeIngredient.Unit.GRAM),
                        createRecipeIngredient(hummus, tahini, 2, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(hummus, garlic, 2, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(hummus, lemon, 1, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(hummus, salt, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(
                                hummus, oliveOil, 2, RecipeIngredient.Unit.TABLESPOON)));
        recipeRepository.save(hummus);

        Recipe pancakes =
                Recipe.builder()
                        .name("Fluffy Pancakes")
                        .description("Light and fluffy pancakes perfect for a weekend breakfast.")
                        .author(david)
                        .imagePath("uploads/recipes/pancakes.jpg")
                        .label(Recipe.Label.QUICK)
                        .steps(
                                Arrays.asList(
                                        "In a large bowl, whisk together flour, sugar, baking"
                                                + " powder, and salt",
                                        "In another bowl, whisk together milk, eggs, and melted"
                                                + " butter",
                                        "Pour wet ingredients into dry ingredients and stir until"
                                                + " just combined (lumps are okay)",
                                        "Heat a griddle or non-stick pan over medium heat and"
                                                + " lightly grease",
                                        "Pour 1/4 cup of batter for each pancake",
                                        "Cook until bubbles form on the surface, then flip and cook"
                                                + " until golden brown",
                                        "Serve warm with maple syrup"))
                        .cookTime(20L)
                        .servings(4)
                        .difficulty(Recipe.Difficulty.EASY)
                        .build();
        pancakes = recipeRepository.save(pancakes);
        pancakes.setRecipeIngredients(
                List.of(
                        createRecipeIngredient(pancakes, flour, 1, RecipeIngredient.Unit.CUP),
                        createRecipeIngredient(pancakes, milk, 1, RecipeIngredient.Unit.CUP),
                        createRecipeIngredient(pancakes, eggs, 2, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(
                                pancakes, bakingPowder, 2, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(
                                pancakes, sugar, 2, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(pancakes, salt, 1, RecipeIngredient.Unit.PINCH),
                        createRecipeIngredient(
                                pancakes, butter, 2, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(
                                pancakes, mapleSyrup, 1, RecipeIngredient.Unit.CUP)));
        recipeRepository.save(pancakes);

        Recipe smoothieBowl =
                Recipe.builder()
                        .name("Tropical Smoothie Bowl")
                        .description(
                                "A refreshing smoothie bowl with mango, banana, and coconut milk.")
                        .author(david)
                        .imagePath("uploads/recipes/smoothie-bowl.jpg")
                        .label(Recipe.Label.DESSERT)
                        .steps(
                                Arrays.asList(
                                        "Freeze mango chunks and banana slices ahead of time",
                                        "In a blender, combine frozen fruits, coconut milk, and"
                                                + " honey",
                                        "Blend until smooth but thick",
                                        "Pour into a bowl",
                                        "Top with granola and fresh fruits",
                                        "Serve immediately"))
                        .cookTime(10L)
                        .servings(2)
                        .difficulty(Recipe.Difficulty.EASY)
                        .build();
        smoothieBowl = recipeRepository.save(smoothieBowl);
        smoothieBowl.setRecipeIngredients(
                List.of(
                        createRecipeIngredient(smoothieBowl, mango, 1, RecipeIngredient.Unit.CUP),
                        createRecipeIngredient(
                                smoothieBowl, banana, 1, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(
                                smoothieBowl, coconutMilk, 1, RecipeIngredient.Unit.CUP),
                        createRecipeIngredient(
                                smoothieBowl, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(
                                smoothieBowl, granola, 1, RecipeIngredient.Unit.CUP)));
        recipeRepository.save(smoothieBowl);

        Recipe frenchToast =
                Recipe.builder()
                        .name("French Toast")
                        .description(
                                "A classic French toast recipe made with thick slices of bread,"
                                        + " eggs, milk, and vanilla.")
                        .author(david)
                        .imagePath("uploads/recipes/french-toast.jpg")
                        .label(Recipe.Label.QUICK)
                        .steps(
                                Arrays.asList(
                                        "In a shallow bowl, whisk together eggs, milk, vanilla, and"
                                            + " cinnamon",
                                        "Dip bread slices into the egg mixture, allowing them to"
                                            + " soak for about 30 seconds on each side",
                                        "Heat butter in a skillet over medium heat",
                                        "Cook the soaked bread until golden brown on both sides,"
                                            + " about 3-4 minutes per side",
                                        "Serve with maple syrup"))
                        .cookTime(15L)
                        .servings(4)
                        .difficulty(Recipe.Difficulty.EASY)
                        .build();
        frenchToast = recipeRepository.save(frenchToast);
        frenchToast.setRecipeIngredients(
                List.of(
                        createRecipeIngredient(frenchToast, bread, 4, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(frenchToast, eggs, 2, RecipeIngredient.Unit.PIECE),
                        createRecipeIngredient(frenchToast, milk, 1, RecipeIngredient.Unit.CUP),
                        createRecipeIngredient(
                                frenchToast, vanilla, 1, RecipeIngredient.Unit.TO_TASTE),
                        createRecipeIngredient(
                                frenchToast, cinnamon, 1, RecipeIngredient.Unit.TO_TASTE),
                        createRecipeIngredient(
                                frenchToast, butter, 2, RecipeIngredient.Unit.TABLESPOON),
                        createRecipeIngredient(
                                frenchToast, mapleSyrup, 1, RecipeIngredient.Unit.CUP)));
        recipeRepository.save(frenchToast);

        // Add likes to recipes
        avocadoToast.like(david);
        avocadoToast.like(amaia);
        recipeRepository.save(avocadoToast);

        hummus.like(midudev);
        hummus.like(amaia);
        hummus.like(david);
        recipeRepository.save(hummus);

        pancakes.like(amaia);
        pancakes.like(midudev);
        recipeRepository.save(pancakes);

        smoothieBowl.like(david);
        smoothieBowl.like(midudev);
        smoothieBowl.like(amaia);
        recipeRepository.save(smoothieBowl);

        // Set user preferences
        david.addPreferredIngredient(avocado);
        david.addPreferredIngredient(mango);
        david.addPreferredIngredient(honey);
        userRepository.save(david);

        midudev.addPreferredIngredient(bread);
        midudev.addPreferredIngredient(eggs);
        midudev.addPreferredIngredient(avocado);
        userRepository.save(midudev);

        amaia.addPreferredIngredient(banana);
        amaia.addPreferredIngredient(honey);
        amaia.addPreferredIngredient(granola);
        userRepository.save(amaia);
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

    private Ingredient createIngredient(String name) {
        Ingredient ingredient = Ingredient.builder().name(name).build();
        return ingredientRepository.save(ingredient);
    }
}
