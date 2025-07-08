-- Clear existing data
TRUNCATE TABLE
  recipe_likes,
  user_preferred_ingredients,
  recipe_ingredients,
  recipes,
  ingredients,
  users
CASCADE;

-- Insert users with different preferences
INSERT INTO users (id, first_name, last_name, username, email, password, role, created_at, avatar_path)
VALUES
    (1, 'David', 'Santos', 'david', 'dqvid01@gmail.com', '$2a$10$lQ1XaYzNa78r.yqP16fcN.bG6LAsVUZ/ugRZTdAnTii7RhMHXkdIG', 'USER', 1704067200000, 'uploads/avatars/default-avatar.webp'),
    (2, 'Silvia', 'García', 'silvia', 'silvia@gmail.com', '$2a$10$lQ1XaYzNa78r.yqP16fcN.bG6LAsVUZ/ugRZTdAnTii7RhMHXkdIG', 'USER', 1704067200000, 'uploads/avatars/default-avatar.webp'),
    (3, 'Iván', 'Santos', 'ivan', 'ivan@gmail.com', '$2a$10$lQ1XaYzNa78r.yqP16fcN.bG6LAsVUZ/ugRZTdAnTii7RhMHXkdIG', 'USER', 1704067200000, 'uploads/avatars/default-avatar.webp'),
    (4, 'Miguel A.', 'Durán', 'midudev', 'midudev@gmail.com', '$2a$10$lQ1XaYzNa78r.yqP16fcN.bG6LAsVUZ/ugRZTdAnTii7RhMHXkdIG', 'USER', 1704067200000, 'uploads/avatars/default-avatar.webp'),
    (5, 'Admin', 'Admin', 'admin', 'admin@example.com', '$2a$10$lQ1XaYzNa78r.yqP16fcN.bG6LAsVUZ/ugRZTdAnTii7RhMHXkdIG', 'ADMIN', 1704067200000, 'uploads/avatars/default-avatar.webp');

-- Insert ingredients
INSERT INTO ingredients (id, name)
VALUES
    (1, 'Avocado'),
    (2, 'Bread'),
    (3, 'Eggs'),
    (4, 'Banana'),
    (5, 'Peanut Butter'),
    (6, 'Yogurt'),
    (7, 'Berries'),
    (8, 'Oats'),
    (9, 'Milk'),
    (10, 'Honey'),
    (11, 'Chickpeas'),
    (12, 'Tahini'),
    (13, 'Lemon'),
    (14, 'Garlic'),
    (15, 'Olive Oil'),
    (16, 'Salt'),
    (17, 'Pepper'),
    (18, 'Cinnamon'),
    (19, 'Maple Syrup'),
    (20, 'White Beans'),
    (21, 'Lentils'),
    (22, 'Mango'),
    (23, 'Granola'),
    (24, 'Flour'),
    (25, 'Baking Powder'),
    (26, 'Sugar'),
    (27, 'Vanilla Extract'),
    (28, 'Coconut Milk'),
    (29, 'Spinach'),
    (30, 'Mixed Greens'),
    (31, 'Vegetables'),
    (32, 'Lemon Juice'),
    (33, 'Onion'),
    (34, 'Herbs'),
    (35, 'Water'),
    (36, 'Lime Juice'),
    (37, 'Fruits'),
    (38, 'Nuts'),
    (39, 'Seeds'),
    (40, 'Rosemary'),
    (41, 'Thyme'),
    (42, 'Vanilla'),
    (43, 'Butter');

-- Insert recipes with different categories and difficulties
INSERT INTO recipes (id, name, description, image_path, cook_time, servings, likes, author_id, difficulty, label, steps)
VALUES
    -- Breakfast Category
    (1, 'Avocado Toast', 'Creamy avocado spread on toasted artisan bread', 'uploads/recipes/avocado-toast.webp', 10, 1, 0, 1, 'EASY', 'QUICK', 
     ARRAY['Toast the bread slices until golden brown', 'Mash the avocado with a fork in a bowl', 'Add a pinch of salt and olive oil to the mashed avocado', 'Spread the avocado mixture evenly on the toast', 'Serve immediately while toast is still warm']),
    
    (2, 'Avocado Egg Toast', 'Protein-packed avocado toast with perfectly cooked eggs', 'uploads/recipes/avocado-egg-toast.webp', 15, 1, 0, 2, 'EASY', 'HEALTHY',
     ARRAY['Toast the bread slices until golden brown', 'Heat olive oil in a pan over medium heat', 'Crack eggs into the pan and cook to your preference', 'Meanwhile, mash the avocado with salt', 'Spread avocado on toast and top with cooked eggs', 'Season with pepper and serve']),
    
    (3, 'Banana Pancakes', 'Fluffy pancakes with caramelized bananas', 'uploads/recipes/banana-pancakes.webp', 20, 2, 0, 3, 'MEDIUM', 'DESSERT',
     ARRAY['Mix flour, baking powder, and sugar in a large bowl', 'In another bowl, whisk together milk and eggs', 'Combine wet and dry ingredients until just mixed', 'Slice bananas and set aside', 'Heat a pan over medium heat and add batter', 'Cook until bubbles form, flip, and cook until golden', 'Serve with sliced bananas on top']),
    
    (4, 'Berry Parfait', 'Layered yogurt parfait with fresh berries and granola', 'uploads/recipes/berry-parfait.webp', 10, 1, 0, 4, 'EASY', 'HEALTHY',
     ARRAY['Wash and prepare fresh berries', 'In a glass or bowl, add a layer of yogurt', 'Add a layer of berries on top of yogurt', 'Sprinkle granola over the berries', 'Repeat layers until glass is full', 'Drizzle with honey and serve immediately']),
    
    (5, 'Berry Smoothie Bowl', 'Thick and creamy smoothie bowl topped with fresh berries', 'uploads/recipes/berry-smoothie-bowl.webp', 15, 1, 0, 5, 'EASY', 'HEALTHY',
     ARRAY['Blend frozen berries, yogurt, and milk until thick', 'Pour smoothie into a bowl', 'Arrange fresh berries on top', 'Add granola for crunch', 'Drizzle with honey', 'Serve immediately with a spoon']),

    -- Lunch/Dinner Category
    (6, 'Chickpea Salad', 'Protein-rich salad with fresh vegetables', 'uploads/recipes/chickpea-salad.webp', 15, 2, 0, 1, 'EASY', 'VEGAN',
     ARRAY['Drain and rinse canned chickpeas', 'Dice vegetables into small pieces', 'Combine chickpeas and vegetables in a large bowl', 'Make dressing with olive oil, lemon juice, salt, and pepper', 'Toss salad with dressing', 'Let sit for 10 minutes before serving']),
    
    (7, 'Floral Hummus', 'Creamy hummus with a beautiful floral presentation', 'uploads/recipes/floral-hummus.webp', 20, 4, 0, 2, 'EASY', 'VEGAN',
     ARRAY['Drain chickpeas and reserve liquid', 'Blend chickpeas, tahini, lemon juice, and garlic', 'Add reserved liquid gradually until smooth', 'Season with salt and pepper', 'Transfer to serving plate', 'Create floral pattern with colorful vegetables', 'Drizzle with olive oil and serve with bread']),
    
    (8, 'French Toast', 'Classic French toast with maple syrup', 'uploads/recipes/french-toast.webp', 20, 2, 0, 3, 'MEDIUM', 'DESSERT',
     ARRAY['Whisk eggs, milk, cinnamon, and vanilla in a shallow dish', 'Heat butter in a large skillet over medium heat', 'Dip bread slices in egg mixture, coating both sides', 'Cook bread in skillet until golden brown on both sides', 'Serve hot with maple syrup and butter']),
    
    (9, 'Lentil Stew', 'Hearty and nutritious lentil stew', 'uploads/recipes/lentil-stew.webp', 45, 4, 0, 4, 'MEDIUM', 'HEALTHY',
     ARRAY['Rinse lentils and set aside', 'Heat olive oil in a large pot', 'Sauté onions and garlic until fragrant', 'Add lentils and enough water to cover', 'Bring to boil, then simmer for 30 minutes', 'Season with salt, pepper, and herbs', 'Cook until lentils are tender and stew thickens']),
    
    (10, 'Mango Avocado Salad', 'Refreshing tropical salad with mango and avocado', 'uploads/recipes/mango-avocado-salad.webp', 15, 2, 0, 5, 'EASY', 'VEGAN',
     ARRAY['Peel and dice mango into cubes', 'Cut avocado into similar-sized pieces', 'Wash and prepare mixed greens', 'Combine mango, avocado, and greens in a bowl', 'Make dressing with lime juice and olive oil', 'Toss gently and serve immediately']),

    -- Breakfast Category (continued)
    (11, 'Overnight Oats', 'Make-ahead breakfast with endless topping possibilities', 'uploads/recipes/overnight-oats.webp', 5, 1, 0, 1, 'EASY', 'HEALTHY',
     ARRAY['Combine oats, milk, and honey in a jar', 'Stir well to combine all ingredients', 'Cover and refrigerate overnight', 'In the morning, stir and add toppings', 'Enjoy cold or warm slightly if preferred']),
    
    (12, 'Classic Pancakes', 'Fluffy and delicious traditional pancakes', 'uploads/recipes/pancakes.webp', 20, 2, 0, 2, 'MEDIUM', 'DESSERT',
     ARRAY['Mix flour, baking powder, sugar, and salt in a bowl', 'In another bowl, whisk milk, eggs, and vanilla', 'Combine wet and dry ingredients until just mixed', 'Heat a griddle or pan over medium heat', 'Pour batter and cook until bubbles form', 'Flip and cook until golden brown', 'Serve hot with syrup']),
    
    (13, 'PB Banana Toast', 'Peanut butter and banana on whole grain toast', 'uploads/recipes/pb-banana-toast.webp', 5, 1, 0, 3, 'EASY', 'QUICK',
     ARRAY['Toast bread until golden brown', 'Spread peanut butter evenly on toast', 'Slice banana into rounds', 'Arrange banana slices on top of peanut butter', 'Drizzle with honey if desired and serve']),
    
    (14, 'Smoothie Bowl', 'Thick and nutritious smoothie bowl with toppings', 'uploads/recipes/smoothie-bowl.webp', 15, 1, 0, 4, 'EASY', 'HEALTHY',
     ARRAY['Blend frozen fruits with yogurt and milk until thick', 'Pour into a bowl', 'Arrange fresh fruits on top', 'Add nuts, seeds, or granola', 'Drizzle with honey or maple syrup', 'Serve immediately']),
    
    (15, 'White Bean Dip', 'Creamy and flavorful white bean dip', 'uploads/recipes/white-bean-dip.webp', 15, 4, 0, 5, 'EASY', 'VEGAN',
     ARRAY['Drain and rinse white beans', 'Blend beans with garlic, lemon juice, and olive oil', 'Season with salt and pepper', 'Add herbs like rosemary or thyme', 'Blend until smooth and creamy', 'Transfer to serving bowl', 'Garnish with olive oil and herbs']),
    
    (16, 'Yogurt Parfait', 'Layered yogurt with granola and fresh fruits', 'uploads/recipes/yogurt-parfait.webp', 10, 1, 0, 1, 'EASY', 'HEALTHY',
     ARRAY['Prepare fresh fruits by washing and cutting', 'In a glass, add a layer of yogurt', 'Add a layer of granola', 'Add a layer of fresh fruits', 'Repeat layers until glass is full', 'Top with a final sprinkle of granola and serve']);

-- Insert recipe ingredients
INSERT INTO recipe_ingredients (id, recipe_id, ingredient_id, quantity, unit)
VALUES
    -- Avocado Toast
    (1, 1, 1, 1, 'PIECE'),
    (2, 1, 2, 2, 'PIECE'),
    (3, 1, 15, 1, 'TABLESPOON'),
    (4, 1, 16, 1, 'PINCH'),

    -- Avocado Egg Toast
    (5, 2, 1, 1, 'PIECE'),
    (6, 2, 2, 2, 'PIECE'),
    (7, 2, 3, 2, 'PIECE'),
    (8, 2, 15, 1, 'TABLESPOON'),
    (9, 2, 16, 1, 'PINCH'),
    (10, 2, 17, 1, 'PINCH'),

    -- Banana Pancakes
    (11, 3, 4, 2, 'PIECE'),
    (12, 3, 24, 200, 'GRAM'),
    (13, 3, 25, 2, 'TABLESPOON'),
    (14, 3, 26, 2, 'TABLESPOON'),
    (15, 3, 9, 250, 'MILLILITER'),
    (16, 3, 3, 1, 'PIECE'),

    -- Berry Parfait
    (17, 4, 6, 200, 'GRAM'),
    (18, 4, 7, 100, 'GRAM'),
    (19, 4, 23, 50, 'GRAM'),
    (20, 4, 10, 1, 'TABLESPOON'),

    -- Berry Smoothie Bowl
    (21, 5, 7, 150, 'GRAM'),
    (22, 5, 6, 100, 'GRAM'),
    (23, 5, 9, 100, 'MILLILITER'),
    (24, 5, 10, 1, 'TABLESPOON'),
    (25, 5, 23, 50, 'GRAM'),

    -- Chickpea Salad
    (26, 6, 11, 400, 'GRAM'),
    (27, 6, 31, 200, 'GRAM'),
    (28, 6, 15, 2, 'TABLESPOON'),
    (29, 6, 32, 2, 'TABLESPOON'),
    (30, 6, 16, 1, 'PINCH'),
    (31, 6, 17, 1, 'PINCH'),

    -- Floral Hummus
    (32, 7, 11, 400, 'GRAM'),
    (33, 7, 12, 3, 'TABLESPOON'),
    (34, 7, 32, 2, 'TABLESPOON'),
    (35, 7, 14, 2, 'PIECE'),
    (36, 7, 15, 2, 'TABLESPOON'),
    (37, 7, 16, 1, 'PINCH'),
    (38, 7, 17, 1, 'PINCH'),
    (39, 7, 31, 100, 'GRAM'),
    (40, 7, 2, 4, 'PIECE'),

    -- French Toast
    (41, 8, 3, 3, 'PIECE'),
    (42, 8, 9, 150, 'MILLILITER'),
    (43, 8, 18, 1, 'PINCH'),
    (44, 8, 42, 1, 'PINCH'),
    (45, 8, 43, 2, 'TABLESPOON'),
    (46, 8, 2, 4, 'PIECE'),
    (47, 8, 19, 3, 'TABLESPOON'),

    -- Lentil Stew
    (48, 9, 21, 300, 'GRAM'),
    (49, 9, 15, 2, 'TABLESPOON'),
    (50, 9, 33, 1, 'PIECE'),
    (51, 9, 14, 3, 'PIECE'),
    (52, 9, 35, 1000, 'MILLILITER'),
    (53, 9, 16, 1, 'PINCH'),
    (54, 9, 17, 1, 'PINCH'),
    (55, 9, 34, 2, 'TABLESPOON'),

    -- Mango Avocado Salad
    (56, 10, 22, 1, 'PIECE'),
    (57, 10, 1, 1, 'PIECE'),
    (58, 10, 30, 100, 'GRAM'),
    (59, 10, 36, 2, 'TABLESPOON'),
    (60, 10, 15, 1, 'TABLESPOON'),

    -- Overnight Oats
    (61, 11, 8, 80, 'GRAM'),
    (62, 11, 9, 150, 'MILLILITER'),
    (63, 11, 10, 1, 'TABLESPOON'),
    (64, 11, 7, 50, 'GRAM'),

    -- Classic Pancakes
    (65, 12, 24, 200, 'GRAM'),
    (66, 12, 25, 2, 'PINCH'),
    (67, 12, 26, 2, 'TABLESPOON'),
    (68, 12, 16, 1, 'PINCH'),
    (69, 12, 9, 250, 'MILLILITER'),
    (70, 12, 3, 2, 'PIECE'),
    (71, 12, 42, 1, 'PINCH'),
    (72, 12, 19, 3, 'TABLESPOON'),

    -- PB Banana Toast
    (73, 13, 2, 2, 'PIECE'),
    (74, 13, 5, 2, 'TABLESPOON'),
    (75, 13, 4, 1, 'PIECE'),
    (76, 13, 10, 1, 'TABLESPOON'),

    -- Smoothie Bowl
    (77, 14, 37, 200, 'GRAM'),
    (78, 14, 6, 150, 'GRAM'),
    (79, 14, 9, 100, 'MILLILITER'),
    (80, 14, 38, 30, 'GRAM'),
    (81, 14, 39, 20, 'GRAM'),
    (82, 14, 23, 50, 'GRAM'),
    (83, 14, 10, 1, 'TABLESPOON'),
    (84, 14, 19, 1, 'TABLESPOON'),

    -- White Bean Dip
    (85, 15, 20, 400, 'GRAM'),
    (86, 15, 14, 2, 'PIECE'),
    (87, 15, 32, 2, 'TABLESPOON'),
    (88, 15, 15, 3, 'TABLESPOON'),
    (89, 15, 16, 1, 'PINCH'),
    (90, 15, 17, 1, 'PINCH'),
    (91, 15, 40, 1, 'PINCH'),
    (92, 15, 41, 1, 'PINCH'),

    -- Yogurt Parfait
    (93, 16, 37, 150, 'GRAM'),
    (94, 16, 6, 200, 'GRAM'),
    (95, 16, 23, 50, 'GRAM'),
    (96, 16, 10, 1, 'TABLESPOON');

-- Set user preferences (each user has distinct preferences)
INSERT INTO user_preferred_ingredients (user_id, ingredient_id)
VALUES
    -- David (Healthy breakfast lover)
    (1, 1),  -- Avocado
    (1, 6),  -- Yogurt
    (1, 8),  -- Oats
    (1, 7),  -- Berries

    -- Silvia (Vegan and healthy)
    (2, 1),  -- Avocado
    (2, 11), -- Chickpeas
    (2, 21), -- Lentils
    (2, 20), -- White Beans

    -- Iván (Sweet tooth)
    (3, 4),  -- Banana
    (3, 5),  -- Peanut Butter
    (3, 19), -- Maple Syrup
    (3, 26), -- Sugar

    -- Miguel (Quick meals)
    (4, 2),  -- Bread
    (4, 3),  -- Eggs
    (4, 8),  -- Oats
    (4, 23), -- Granola

    -- Admin (Balanced diet)
    (5, 1),  -- Avocado
    (5, 6),  -- Yogurt
    (5, 11), -- Chickpeas
    (5, 21); -- Lentils

-- Create likes to showcase recommendation system
INSERT INTO recipe_likes (user_id, recipe_id)
VALUES
    -- David likes healthy breakfasts
    (1, 1),  -- Avocado Toast
    (1, 4),  -- Berry Parfait
    (1, 11), -- Overnight Oats

    -- Silvia likes vegan dishes
    (2, 6),  -- Chickpea Salad
    (2, 7),  -- Floral Hummus
    (2, 10), -- Mango Avocado Salad

    -- Iván likes sweet breakfasts
    (3, 3),  -- Banana Pancakes
    (3, 8),  -- French Toast
    (3, 12), -- Classic Pancakes

    -- Miguel likes quick meals
    (4, 1),  -- Avocado Toast
    (4, 13), -- PB Banana Toast
    (4, 16), -- Yogurt Parfait

    -- Admin likes healthy options
    (5, 2),  -- Avocado Egg Toast
    (5, 5),  -- Berry Smoothie Bowl
    (5, 9);  -- Lentil Stew

-- Update recipe likes count
UPDATE recipes SET likes = 2 WHERE id = 1;  -- Avocado Toast
UPDATE recipes SET likes = 1 WHERE id = 2;  -- Avocado Egg Toast
UPDATE recipes SET likes = 1 WHERE id = 3;  -- Banana Pancakes
UPDATE recipes SET likes = 1 WHERE id = 4;  -- Berry Parfait
UPDATE recipes SET likes = 1 WHERE id = 5;  -- Berry Smoothie Bowl
UPDATE recipes SET likes = 1 WHERE id = 6;  -- Chickpea Salad
UPDATE recipes SET likes = 1 WHERE id = 7;  -- Floral Hummus
UPDATE recipes SET likes = 1 WHERE id = 8;  -- French Toast
UPDATE recipes SET likes = 1 WHERE id = 9;  -- Lentil Stew
UPDATE recipes SET likes = 1 WHERE id = 10; -- Mango Avocado Salad
UPDATE recipes SET likes = 1 WHERE id = 11; -- Overnight Oats
UPDATE recipes SET likes = 1 WHERE id = 12; -- Classic Pancakes
UPDATE recipes SET likes = 1 WHERE id = 13; -- PB Banana Toast
UPDATE recipes SET likes = 0 WHERE id = 14; -- Smoothie Bowl
UPDATE recipes SET likes = 0 WHERE id = 15; -- White Bean Dip
UPDATE recipes SET likes = 1 WHERE id = 16; -- Yogurt Parfait

-- Reset sequences to avoid duplicate key violations
-- This ensures that auto-generated IDs start from the correct value
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('ingredients_id_seq', (SELECT MAX(id) FROM ingredients));
SELECT setval('recipes_id_seq', (SELECT MAX(id) FROM recipes));
SELECT setval('recipe_ingredients_id_seq', (SELECT MAX(id) FROM recipe_ingredients));

-- Verification queries to ensure everything is properly set up
-- Show ingredient count per recipe
SELECT
    r.id,
    r.name as recipe_name,
    COUNT(ri.ingredient_id) as ingredient_count
FROM recipes r
LEFT JOIN recipe_ingredients ri ON r.id = ri.recipe_id
GROUP BY r.id, r.name
ORDER BY r.id;

-- Show ingredient usage statistics
SELECT
    i.id,
    i.name,
    COUNT(ri.recipe_id) as used_in_recipes
FROM ingredients i
LEFT JOIN recipe_ingredients ri ON i.id = ri.ingredient_id
GROUP BY i.id, i.name
ORDER BY used_in_recipes DESC, i.name; 