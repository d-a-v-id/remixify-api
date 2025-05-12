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
    (30, 'Mixed Greens');

-- Insert recipes with different categories and difficulties
INSERT INTO recipes (id, name, description, image_path, cook_time, servings, likes, author_id, difficulty, label)
VALUES
    -- Breakfast Category
    (1, 'Avocado Toast', 'Creamy avocado spread on toasted artisan bread', 'uploads/recipes/avocado-toast.webp', 10, 1, 0, 1, 'EASY', 'QUICK'),
    (2, 'Avocado Egg Toast', 'Protein-packed avocado toast with perfectly cooked eggs', 'uploads/recipes/avocado-egg-toast.webp', 15, 1, 0, 2, 'EASY', 'HEALTHY'),
    (3, 'Banana Pancakes', 'Fluffy pancakes with caramelized bananas', 'uploads/recipes/banana-pancakes.webp', 20, 2, 0, 3, 'MEDIUM', 'DESSERT'),
    (4, 'Berry Parfait', 'Layered yogurt parfait with fresh berries and granola', 'uploads/recipes/berry-parfait.webp', 10, 1, 0, 4, 'EASY', 'HEALTHY'),
    (5, 'Berry Smoothie Bowl', 'Thick and creamy smoothie bowl topped with fresh berries', 'uploads/recipes/berry-smoothie-bowl.webp', 15, 1, 0, 5, 'EASY', 'HEALTHY'),

    -- Lunch/Dinner Category
    (6, 'Chickpea Salad', 'Protein-rich salad with fresh vegetables', 'uploads/recipes/chickpea-salad.webp', 15, 2, 0, 1, 'EASY', 'VEGAN'),
    (7, 'Floral Hummus', 'Creamy hummus with a beautiful floral presentation', 'uploads/recipes/floral-hummus.webp', 20, 4, 0, 2, 'EASY', 'VEGAN'),
    (8, 'French Toast', 'Classic French toast with maple syrup', 'uploads/recipes/french-toast.webp', 20, 2, 0, 3, 'MEDIUM', 'DESSERT'),
    (9, 'Lentil Stew', 'Hearty and nutritious lentil stew', 'uploads/recipes/lentil-stew.webp', 45, 4, 0, 4, 'MEDIUM', 'HEALTHY'),
    (10, 'Mango Avocado Salad', 'Refreshing tropical salad with mango and avocado', 'uploads/recipes/mango-avocado-salad.webp', 15, 2, 0, 5, 'EASY', 'VEGAN'),

    -- Breakfast Category (continued)
    (11, 'Overnight Oats', 'Make-ahead breakfast with endless topping possibilities', 'uploads/recipes/overnight-oats.webp', 5, 1, 0, 1, 'EASY', 'HEALTHY'),
    (12, 'Classic Pancakes', 'Fluffy and delicious traditional pancakes', 'uploads/recipes/pancakes.webp', 20, 2, 0, 2, 'MEDIUM', 'DESSERT'),
    (13, 'PB Banana Toast', 'Peanut butter and banana on whole grain toast', 'uploads/recipes/pb-banana-toast.webp', 5, 1, 0, 3, 'EASY', 'QUICK'),
    (14, 'Smoothie Bowl', 'Thick and nutritious smoothie bowl with toppings', 'uploads/recipes/smoothie-bowl.webp', 15, 1, 0, 4, 'EASY', 'HEALTHY'),
    (15, 'White Bean Dip', 'Creamy and flavorful white bean dip', 'uploads/recipes/white-bean-dip.webp', 15, 4, 0, 5, 'EASY', 'VEGAN'),
    (16, 'Yogurt Parfait', 'Layered yogurt with granola and fresh fruits', 'uploads/recipes/yogurt-parfait.webp', 10, 1, 0, 1, 'EASY', 'HEALTHY');

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

    -- Banana Pancakes
    (9, 3, 4, 2, 'PIECE'),
    (10, 3, 24, 200, 'GRAM'),
    (11, 3, 25, 2, 'TABLESPOON'),
    (12, 3, 26, 2, 'TABLESPOON'),
    (13, 3, 9, 250, 'MILLILITER'),

    -- Berry Parfait
    (14, 4, 6, 200, 'GRAM'),
    (15, 4, 7, 100, 'GRAM'),
    (16, 4, 23, 50, 'GRAM'),
    (17, 4, 10, 1, 'TABLESPOON'),

    -- Berry Smoothie Bowl
    (18, 5, 7, 150, 'GRAM'),
    (19, 5, 6, 100, 'GRAM'),
    (20, 5, 9, 100, 'MILLILITER'),
    (21, 5, 10, 1, 'TABLESPOON');

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
UPDATE recipes SET likes = 0 WHERE id = 7;  -- Floral Hummus
UPDATE recipes SET likes = 1 WHERE id = 8;  -- French Toast
UPDATE recipes SET likes = 1 WHERE id = 9;  -- Lentil Stew
UPDATE recipes SET likes = 1 WHERE id = 10; -- Mango Avocado Salad
UPDATE recipes SET likes = 1 WHERE id = 11; -- Overnight Oats
UPDATE recipes SET likes = 1 WHERE id = 12; -- Classic Pancakes
UPDATE recipes SET likes = 1 WHERE id = 13; -- PB Banana Toast
UPDATE recipes SET likes = 0 WHERE id = 14; -- Smoothie Bowl
UPDATE recipes SET likes = 0 WHERE id = 15; -- White Bean Dip
UPDATE recipes SET likes = 1 WHERE id = 16; -- Yogurt Parfait