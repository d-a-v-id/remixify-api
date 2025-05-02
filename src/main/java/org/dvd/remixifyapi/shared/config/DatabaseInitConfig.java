package org.dvd.remixifyapi.shared.config;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile({"dev", "pro"})
@RequiredArgsConstructor
public class DatabaseInitConfig {

        private final UserRepository userRepository;
        private final RecipeRepository recipeRepository;
        private final IngredientRepository ingredientRepository;
        private final RecipeLikeService recipeLikeService;
        private final PasswordEncoder passwordEncoder;

        @PostConstruct
        @Transactional
        public void init() {
                // Create users
                User david = User.builder()
                                .firstName("David")
                                .lastName("Santos")
                                .username("david")
                                .email("dqvid01@gmail.com")
                                .password(passwordEncoder.encode("password"))
                                .build();
                userRepository.save(david);

                User midudev = User.builder()
                                .firstName("Miguel A.")
                                .lastName("Durán")
                                .username("midudev")
                                .email("midudev@mail.com")
                                .password(passwordEncoder.encode("password"))
                                .build();
                userRepository.save(midudev);

                User amaia = User.builder()
                                .firstName("Amaia")
                                .lastName("Romero")
                                .username("amaia")
                                .email("amaia@mail.com")
                                .password(passwordEncoder.encode("password"))
                                .build();
                userRepository.save(amaia);

                User admin = User.builder()
                                .firstName("Admin")
                                .lastName("")
                                .username("admin")
                                .email("admin@mail.com")
                                .password(passwordEncoder.encode("password"))
                                .role(User.Role.ADMIN)
                                .build();
                userRepository.save(admin);

                // Create ingredients with clear categories for recommendation patterns
                // Breakfast ingredients
                Ingredient avocado = createIngredient("Avocado");
                Ingredient bread = createIngredient("Whole Grain Bread");
                Ingredient eggs = createIngredient("Eggs");
                Ingredient salt = createIngredient("Salt");
                Ingredient pepper = createIngredient("Black Pepper");
                Ingredient oliveOil = createIngredient("Olive Oil");
                Ingredient lemon = createIngredient("Lemon");

                // Dips and spreads ingredients
                Ingredient chickpeas = createIngredient("Chickpeas");
                Ingredient tahini = createIngredient("Tahini");
                Ingredient garlic = createIngredient("Garlic");
                Ingredient cannellini = createIngredient("Cannellini Beans");
                Ingredient rosemary = createIngredient("Rosemary");

                // Baking ingredients
                Ingredient flour = createIngredient("All-Purpose Flour");
                Ingredient bakingPowder = createIngredient("Baking Powder");
                Ingredient sugar = createIngredient("Sugar");
                Ingredient butter = createIngredient("Butter");
                Ingredient mapleSyrup = createIngredient("Maple Syrup");

                // Smoothie and bowl ingredients
                Ingredient mango = createIngredient("Mango");
                Ingredient banana = createIngredient("Banana");
                Ingredient coconutMilk = createIngredient("Coconut Milk");
                Ingredient honey = createIngredient("Honey");
                Ingredient granola = createIngredient("Granola");
                Ingredient blueberries = createIngredient("Blueberries");
                Ingredient strawberries = createIngredient("Strawberries");
                Ingredient yogurt = createIngredient("Greek Yogurt");

                // Additional breakfast ingredients
                Ingredient milk = createIngredient("Milk");
                Ingredient vanilla = createIngredient("Vanilla Extract");
                Ingredient cinnamon = createIngredient("Cinnamon");
                Ingredient oats = createIngredient("Rolled Oats");
                Ingredient almonds = createIngredient("Almonds");
                Ingredient peanutButter = createIngredient("Peanut Butter");

                // BREAKFAST RECIPES
                // Avocado Toast
                Recipe avocadoToast = Recipe.builder()
                                .name("Avocado Toast")
                                .description("A simple and delicious breakfast option with creamy avocado on toasted bread.")
                                .imagePath("uploads/recipes/avocado-toast.jpg")
                                .author(midudev)
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "Toast the bread slices until golden and crispy",
                                                "Mash the avocado with a fork and mix with lemon juice, salt, and pepper",
                                                "Spread the mashed avocado on the toast",
                                                "Drizzle with olive oil and sprinkle with salt and pepper"))
                                .cookTime(10L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                avocadoToast = recipeRepository.save(avocadoToast);
                avocadoToast.setRecipeIngredients(List.of(
                                createRecipeIngredient(avocadoToast, avocado, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(avocadoToast, bread, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(avocadoToast, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(avocadoToast, pepper, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(avocadoToast, oliveOil, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(avocadoToast, lemon, 1, RecipeIngredient.Unit.PIECE)));
                avocadoToast = recipeRepository.save(avocadoToast);

                // Avocado Egg Toast - Similar to Avocado Toast but with eggs (for recommending
                // to avocado toast lovers)
                Recipe avocadoEggToast = Recipe.builder()
                                .name("Avocado Egg Toast")
                                .description("Creamy avocado toast topped with perfectly fried eggs - a protein-packed breakfast upgrade.")
                                .imagePath("uploads/recipes/avocado-egg-toast.jpg")
                                .author(david)
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "Toast the bread slices until golden and crispy",
                                                "Mash the avocado with lemon juice, salt, and pepper",
                                                "Spread the mashed avocado on the toast",
                                                "Fry eggs in a pan with olive oil until whites are set but yolks are runny",
                                                "Place eggs on top of the avocado toast",
                                                "Season with salt and pepper"))
                                .cookTime(15L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                avocadoEggToast = recipeRepository.save(avocadoEggToast);
                avocadoEggToast.setRecipeIngredients(List.of(
                                createRecipeIngredient(avocadoEggToast, avocado, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(avocadoEggToast, bread, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(avocadoEggToast, eggs, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(avocadoEggToast, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(avocadoEggToast, pepper, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(avocadoEggToast, oliveOil, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(avocadoEggToast, lemon, 1, RecipeIngredient.Unit.PIECE)));
                recipeRepository.save(avocadoEggToast);

                // DIPS AND SPREADS RECIPES
                // Hummus
                Recipe hummus = Recipe.builder()
                                .name("Floral Green Hummus")
                                .description("A creamy and delicious hummus made with chickpeas, tahini, and garlic.")
                                .imagePath("uploads/recipes/floral-hummus.jpg")
                                .author(david)
                                .label(Recipe.Label.VEGAN)
                                .steps(Arrays.asList(
                                                "Drain and rinse the chickpeas",
                                                "In a food processor, combine chickpeas, tahini, minced garlic, and lemon juice",
                                                "Add salt and olive oil",
                                                "Blend until smooth, adding water if needed for desired consistency",
                                                "Taste and adjust seasonings as needed",
                                                "Transfer to a serving bowl and drizzle with olive oil"))
                                .cookTime(15L)
                                .servings(6)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                hummus = recipeRepository.save(hummus);
                hummus.setRecipeIngredients(List.of(
                                createRecipeIngredient(hummus, chickpeas, 5, RecipeIngredient.Unit.GRAM),
                                createRecipeIngredient(hummus, tahini, 2, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(hummus, garlic, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(hummus, lemon, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(hummus, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(hummus, oliveOil, 2, RecipeIngredient.Unit.TABLESPOON)));
                hummus = recipeRepository.save(hummus);

                // White Bean Dip - Similar to hummus but with cannellini beans (for
                // recommending to hummus lovers)
                Recipe whiteBeanDip = Recipe.builder()
                                .name("Rosemary White Bean Dip")
                                .description("A smooth and aromatic dip made with cannellini beans, fresh rosemary, and garlic.")
                                .imagePath("uploads/recipes/white-bean-dip.jpg")
                                .author(midudev)
                                .label(Recipe.Label.VEGAN)
                                .steps(Arrays.asList(
                                                "Drain and rinse the cannellini beans",
                                                "In a food processor, combine beans, minced garlic, and lemon juice",
                                                "Add fresh rosemary, salt and olive oil",
                                                "Blend until smooth, adding water if needed for desired consistency",
                                                "Taste and adjust seasonings as needed",
                                                "Transfer to a serving bowl and drizzle with olive oil"))
                                .cookTime(15L)
                                .servings(6)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                whiteBeanDip = recipeRepository.save(whiteBeanDip);
                whiteBeanDip.setRecipeIngredients(List.of(
                                createRecipeIngredient(whiteBeanDip, cannellini, 5, RecipeIngredient.Unit.GRAM),
                                createRecipeIngredient(whiteBeanDip, rosemary, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(whiteBeanDip, garlic, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(whiteBeanDip, lemon, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(whiteBeanDip, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(whiteBeanDip, oliveOil, 2, RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(whiteBeanDip);

                // PANCAKES AND BREAKFAST CARBS
                // Pancakes
                Recipe pancakes = Recipe.builder()
                                .name("Fluffy Pancakes")
                                .description("Light and fluffy pancakes perfect for a weekend breakfast.")
                                .author(david)
                                .imagePath("uploads/recipes/pancakes.jpg")
                                .label(Recipe.Label.QUICK)
                                .steps(Arrays.asList(
                                                "In a large bowl, whisk together flour, sugar, baking powder, and salt",
                                                "In another bowl, whisk together milk, eggs, and melted butter",
                                                "Pour wet ingredients into dry ingredients and stir until just combined (lumps are okay)",
                                                "Heat a griddle or non-stick pan over medium heat and lightly grease",
                                                "Pour 1/4 cup of batter for each pancake",
                                                "Cook until bubbles form on the surface, then flip and cook until golden brown",
                                                "Serve warm with maple syrup"))
                                .cookTime(20L)
                                .servings(4)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                pancakes = recipeRepository.save(pancakes);
                pancakes.setRecipeIngredients(List.of(
                                createRecipeIngredient(pancakes, flour, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(pancakes, milk, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(pancakes, eggs, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(pancakes, bakingPowder, 2, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(pancakes, sugar, 2, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(pancakes, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(pancakes, butter, 2, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(pancakes, mapleSyrup, 1, RecipeIngredient.Unit.CUP)));
                pancakes = recipeRepository.save(pancakes);

                // Banana Pancakes - Similar to pancakes but with bananas (for pancake lovers
                // and banana enthusiasts)
                Recipe bananaPancakes = Recipe.builder()
                                .name("Banana Oat Pancakes")
                                .description("Healthy and delicious pancakes made with mashed bananas and oats - naturally sweet!")
                                .author(amaia)
                                .imagePath("uploads/recipes/banana-pancakes.jpg")
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "In a blender, combine oats, mashed banana, eggs, milk, and cinnamon",
                                                "Blend until smooth and let sit for 5 minutes to thicken",
                                                "Heat a non-stick pan over medium heat and lightly grease",
                                                "Pour 1/4 cup of batter for each pancake",
                                                "Cook until bubbles form on the surface, then flip and cook until golden brown",
                                                "Serve warm with sliced bananas and honey"))
                                .cookTime(20L)
                                .servings(4)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                bananaPancakes = recipeRepository.save(bananaPancakes);
                bananaPancakes.setRecipeIngredients(List.of(
                                createRecipeIngredient(bananaPancakes, oats, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(bananaPancakes, banana, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(bananaPancakes, milk, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(bananaPancakes, eggs, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(bananaPancakes, cinnamon, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(bananaPancakes, honey, 2, RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(bananaPancakes);

                // SMOOTHIE BOWLS AND BREAKFAST BOWLS
                // Tropical Smoothie Bowl
                Recipe smoothieBowl = Recipe.builder()
                                .name("Tropical Smoothie Bowl")
                                .description("A refreshing smoothie bowl with mango, banana, and coconut milk.")
                                .author(david)
                                .imagePath("uploads/recipes/smoothie-bowl.jpg")
                                .label(Recipe.Label.DESSERT)
                                .steps(Arrays.asList(
                                                "Freeze mango chunks and banana slices ahead of time",
                                                "In a blender, combine frozen fruits, coconut milk, and honey",
                                                "Blend until smooth but thick",
                                                "Pour into a bowl",
                                                "Top with granola and fresh fruits",
                                                "Serve immediately"))
                                .cookTime(10L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                smoothieBowl = recipeRepository.save(smoothieBowl);
                smoothieBowl.setRecipeIngredients(List.of(
                                createRecipeIngredient(smoothieBowl, mango, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(smoothieBowl, banana, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(smoothieBowl, coconutMilk, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(smoothieBowl, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(smoothieBowl, granola, 1, RecipeIngredient.Unit.CUP)));
                smoothieBowl = recipeRepository.save(smoothieBowl);

                // Berry Smoothie Bowl - Similar to tropical smoothie bowl but with berries
                Recipe berrySmoothieBowl = Recipe.builder()
                                .name("Berry Blast Smoothie Bowl")
                                .description("A vibrant and antioxidant-rich smoothie bowl with blueberries, strawberries, and Greek yogurt.")
                                .author(amaia)
                                .imagePath("uploads/recipes/berry-smoothie-bowl.jpg")
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "Freeze berries ahead of time",
                                                "In a blender, combine frozen berries, banana, yogurt, and honey",
                                                "Blend until smooth but thick",
                                                "Pour into a bowl",
                                                "Top with granola, fresh berries, and a drizzle of honey",
                                                "Serve immediately"))
                                .cookTime(10L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                berrySmoothieBowl = recipeRepository.save(berrySmoothieBowl);
                berrySmoothieBowl.setRecipeIngredients(List.of(
                                createRecipeIngredient(berrySmoothieBowl, blueberries, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berrySmoothieBowl, strawberries, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berrySmoothieBowl, banana, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(berrySmoothieBowl, yogurt, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berrySmoothieBowl, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(berrySmoothieBowl, granola, 1, RecipeIngredient.Unit.CUP)));
                recipeRepository.save(berrySmoothieBowl);

                // Overnight Oats - For users who like breakfast bowls and smoothie ingredients
                Recipe overnightOats = Recipe.builder()
                                .name("Banana Honey Overnight Oats")
                                .description("Easy make-ahead breakfast with oats, banana, honey, and almonds - perfect for busy mornings.")
                                .author(amaia)
                                .imagePath("uploads/recipes/overnight-oats.jpg")
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "In a jar, combine oats and milk",
                                                "Add sliced banana, honey, and a pinch of cinnamon",
                                                "Stir well, cover, and refrigerate overnight",
                                                "In the morning, top with chopped almonds and additional banana slices",
                                                "Enjoy cold or warm"))
                                .cookTime(5L) // Plus overnight refrigeration
                                .servings(1)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                overnightOats = recipeRepository.save(overnightOats);
                overnightOats.setRecipeIngredients(List.of(
                                createRecipeIngredient(overnightOats, oats, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(overnightOats, milk, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(overnightOats, banana, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(overnightOats, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(overnightOats, cinnamon, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(overnightOats, almonds, 2, RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(overnightOats);

                // SALADS AND SIDES
                // Mango Avocado Salad
                Recipe mangoAvocadoSalad = Recipe.builder()
                                .name("Mango Avocado Salad")
                                .description("A refreshing salad with ripe mango, creamy avocado, lime juice, and a hint of honey.")
                                .author(amaia)
                                .imagePath("uploads/recipes/mango-avocado-salad.jpg")
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "Dice the mango and avocado.",
                                                "Mix with lemon juice, olive oil, and honey.",
                                                "Season with salt and toss gently.",
                                                "Serve chilled."))
                                .cookTime(10L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                mangoAvocadoSalad = recipeRepository.save(mangoAvocadoSalad);
                mangoAvocadoSalad.setRecipeIngredients(List.of(
                                createRecipeIngredient(mangoAvocadoSalad, mango, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(mangoAvocadoSalad, avocado, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(mangoAvocadoSalad, lemon, 1, RecipeIngredient.Unit.TO_TASTE),
                                createRecipeIngredient(mangoAvocadoSalad, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(mangoAvocadoSalad, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(mangoAvocadoSalad, oliveOil, 1,
                                                RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(mangoAvocadoSalad);

                // Chickpea Salad
                Recipe chickpeaSalad = Recipe.builder()
                                .name("Mediterranean Chickpea Salad")
                                .description("Chickpeas, cucumber, tomatoes, lemon, olive oil, and a bit of garlic for a protein-packed salad.")
                                .author(midudev)
                                .imagePath("uploads/recipes/chickpea-salad.jpg")
                                .label(Recipe.Label.HEALTHY)
                                .steps(Arrays.asList(
                                                "Rinse and drain chickpeas.",
                                                "Chop cucumber and tomatoes.",
                                                "Mix all ingredients with lemon juice and olive oil.",
                                                "Add minced garlic, salt, and pepper to taste.",
                                                "Serve as a side or light meal."))
                                .cookTime(10L)
                                .servings(2)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                chickpeaSalad = recipeRepository.save(chickpeaSalad);
                chickpeaSalad.setRecipeIngredients(List.of(
                                createRecipeIngredient(chickpeaSalad, chickpeas, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(chickpeaSalad, lemon, 1, RecipeIngredient.Unit.TO_TASTE),
                                createRecipeIngredient(chickpeaSalad, oliveOil, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(chickpeaSalad, garlic, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(chickpeaSalad, salt, 1, RecipeIngredient.Unit.PINCH),
                                createRecipeIngredient(chickpeaSalad, pepper, 1, RecipeIngredient.Unit.PINCH)));
                recipeRepository.save(chickpeaSalad);

                // DESSERTS AND SWEETS
                // Yogurt Parfait
                Recipe parfait = Recipe.builder()
                                .name("Tropical Yogurt Parfait")
                                .description("Layers of yogurt, mango, banana, granola, and a drizzle of honey.")
                                .author(amaia)
                                .imagePath("uploads/recipes/yogurt-parfait.jpg")
                                .label(Recipe.Label.DESSERT)
                                .steps(Arrays.asList(
                                                "Layer yogurt, mango, and banana in a glass.",
                                                "Top with granola and drizzle with honey.",
                                                "Serve immediately for a refreshing breakfast or snack."))
                                .cookTime(5L)
                                .servings(1)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                parfait = recipeRepository.save(parfait);
                parfait.setRecipeIngredients(List.of(
                                createRecipeIngredient(parfait, yogurt, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(parfait, mango, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(parfait, banana, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(parfait, granola, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(parfait, honey, 1, RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(parfait);

                // Berry Parfait - Similar to tropical parfait but with berries
                Recipe berryParfait = Recipe.builder()
                                .name("Berry Yogurt Parfait")
                                .description("Creamy layers of yogurt with fresh berries, honey, and crunchy granola.")
                                .author(david)
                                .imagePath("uploads/recipes/berry-parfait.jpg")
                                .label(Recipe.Label.DESSERT)
                                .steps(Arrays.asList(
                                                "Layer yogurt, blueberries, and strawberries in a glass.",
                                                "Add a layer of granola.",
                                                "Repeat layers ending with yogurt on top.",
                                                "Drizzle with honey and top with fresh berries.",
                                                "Serve immediately."))
                                .cookTime(5L)
                                .servings(1)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                berryParfait = recipeRepository.save(berryParfait);
                berryParfait.setRecipeIngredients(List.of(
                                createRecipeIngredient(berryParfait, yogurt, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berryParfait, blueberries, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berryParfait, strawberries, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berryParfait, granola, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(berryParfait, honey, 1, RecipeIngredient.Unit.TABLESPOON)));
                recipeRepository.save(berryParfait);

                // French Toast
                Recipe frenchToast = Recipe.builder()
                                .name("French Toast")
                                .description("A classic French toast recipe made with thick slices of bread, eggs, milk, and vanilla.")
                                .author(david)
                                .imagePath("uploads/recipes/french-toast.jpg")
                                .label(Recipe.Label.QUICK)
                                .steps(Arrays.asList(
                                                "In a shallow bowl, whisk together eggs, milk, vanilla, and cinnamon",
                                                "Dip bread slices into the egg mixture, allowing them to soak for about 30 seconds on each side",
                                                "Heat butter in a skillet over medium heat",
                                                "Cook the soaked bread until golden brown on both sides, about 3-4 minutes per side",
                                                "Serve with maple syrup"))
                                .cookTime(15L)
                                .servings(4)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                frenchToast = recipeRepository.save(frenchToast);
                frenchToast.setRecipeIngredients(List.of(
                                createRecipeIngredient(frenchToast, bread, 4, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(frenchToast, eggs, 2, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(frenchToast, milk, 1, RecipeIngredient.Unit.CUP),
                                createRecipeIngredient(frenchToast, vanilla, 1, RecipeIngredient.Unit.TO_TASTE),
                                createRecipeIngredient(frenchToast, cinnamon, 1, RecipeIngredient.Unit.TO_TASTE),
                                createRecipeIngredient(frenchToast, butter, 2, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(frenchToast, mapleSyrup, 1, RecipeIngredient.Unit.CUP)));
                recipeRepository.save(frenchToast);

                // Peanut Butter Banana Toast - For users who like banana and toast combinations
                Recipe pbBananaToast = Recipe.builder()
                                .name("Peanut Butter Banana Toast")
                                .description("Simple yet satisfying breakfast with whole grain toast, creamy peanut butter, and fresh banana slices.")
                                .author(midudev)
                                .imagePath("uploads/recipes/pb-banana-toast.jpg")
                                .label(Recipe.Label.QUICK)
                                .steps(Arrays.asList(
                                                "Toast the bread slices until golden and crispy",
                                                "Spread peanut butter evenly on the warm toast",
                                                "Arrange banana slices on top",
                                                "Drizzle with honey",
                                                "Optionally sprinkle with cinnamon"))
                                .cookTime(5L)
                                .servings(1)
                                .difficulty(Recipe.Difficulty.EASY)
                                .build();
                pbBananaToast = recipeRepository.save(pbBananaToast);
                pbBananaToast.setRecipeIngredients(List.of(
                                createRecipeIngredient(pbBananaToast, bread, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(pbBananaToast, peanutButter, 2,
                                                RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(pbBananaToast, banana, 1, RecipeIngredient.Unit.PIECE),
                                createRecipeIngredient(pbBananaToast, honey, 1, RecipeIngredient.Unit.TABLESPOON),
                                createRecipeIngredient(pbBananaToast, cinnamon, 1, RecipeIngredient.Unit.PINCH)));
                recipeRepository.save(pbBananaToast);

                // ESTABLISH CLEAR USER PREFERENCE PATTERNS FOR RECOMMENDATIONS

                // David - Preference for avocado, mango, honey dishes, and breakfast items
                recipeLikeService.likeRecipe(avocadoToast, david);
                recipeLikeService.likeRecipe(hummus, david);
                recipeLikeService.likeRecipe(smoothieBowl, david);
                recipeLikeService.likeRecipe(mangoAvocadoSalad, david);
                recipeLikeService.likeRecipe(frenchToast, david);

                // Midudev - Preference for bread dishes
                recipeLikeService.likeRecipe(avocadoToast, midudev);
                recipeLikeService.likeRecipe(pbBananaToast, midudev);
                recipeLikeService.likeRecipe(frenchToast, midudev);
                recipeLikeService.likeRecipe(chickpeaSalad, midudev);

                // Amaia - Preference for fruit recipes and breakfast bowls
                recipeLikeService.likeRecipe(smoothieBowl, amaia);
                recipeLikeService.likeRecipe(parfait, amaia);
                recipeLikeService.likeRecipe(bananaPancakes, amaia);
                recipeLikeService.likeRecipe(overnightOats, amaia);
                recipeLikeService.likeRecipe(mangoAvocadoSalad, amaia);

                userRepository.saveAll(List.of(david, midudev, amaia));
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