-- Insert users (passwords are encoded 'password')
INSERT INTO users (first_name, last_name, username, email, password, role, created_at) VALUES
('Silvia', 'García', 'mama', 'silvia@gmail.com', '$2a$10$K653U/97YyWvW1qHOWjMEOhN3viVukrEvbzWAxziWuYKyZrP/FWCi', 'USER', 1715000000000),
('Miguel A.', 'Durán', 'midudev', 'midudev@mail.com', '$2a$10$VsM6tV3zSZrBF134IhUEVui/h7MXF14PemlUFyTudASaumoLinfZe', 'USER', 1715000000000),
('David', 'Santos', 'david', 'dqvid01@gmail.com', '$2a$10$7MROLIKj/Pvr6q1yqnLyp.6uYom9w7Cv/CINcMn3EdR5x49YZ.F3q', 'USER', 1715000000000),
('Admin', '', 'admin', 'admin@mail.com', '$2a$10$rk4FI7tjSqJlpii9exYuA./vRzkcSsaCsaB9bxYxI9sCzXY/zCoSK', 'ADMIN', 1715000000000);

-- Insert ingredients
INSERT INTO ingredients (name) VALUES
('Avocado'),
('Whole Grain Bread'),
('Eggs'),
('Salt'),
('Black Pepper'),
('Olive Oil'),
('Lemon'),
('Chickpeas'),
('Tahini'),
('Garlic'),
('Cannellini Beans'),
('Rosemary'),
('All-Purpose Flour'),
('Baking Powder'),
('Sugar'),
('Butter'),
('Maple Syrup'),
('Mango'),
('Banana'),
('Coconut Milk'),
('Honey'),
('Granola'),
('Blueberries'),
('Strawberries'),
('Greek Yogurt'),
('Milk'),
('Vanilla Extract'),
('Cinnamon'),
('Rolled Oats'),
('Almonds'),
('Peanut Butter'),
('Cocoa Powder'),
('Dark Chocolate'),
('Coffee'),
('Mint'),
('Lime'),
('Cilantro'),
('Red Onion'),
('Tomato'),
('Cucumber'),
('Feta Cheese'),
('Kalamata Olives'),
('Red Wine Vinegar'),
('Balsamic Vinegar'),
('Soy Sauce'),
('Ginger'),
('Sesame Oil'),
('Rice Vinegar'),
('Nori'),
('Sushi Rice'),
('Salmon');

-- Insert recipes
INSERT INTO recipes (name, description, image_path, author_id, label, steps, cook_time, servings, difficulty, likes) VALUES
('Avocado Toast', 'A simple and delicious breakfast option with creamy avocado on toasted bread.', 'uploads/recipes/avocado-toast.webp', 1, 'HEALTHY', '["Toast the bread slices until golden and crispy", "Mash the avocado with a fork and mix with lemon juice, salt, and pepper", "Spread the mashed avocado on the toast", "Drizzle with olive oil and sprinkle with salt and pepper"]', 10, 2, 'EASY', 0),
('Avocado Egg Toast', 'Creamy avocado toast topped with perfectly fried eggs - a protein-packed breakfast upgrade.', 'uploads/recipes/avocado-egg-toast.webp', 2, 'HEALTHY', '["Toast the bread slices until golden and crispy", "Mash the avocado with lemon juice, salt, and pepper", "Spread the mashed avocado on the toast", "Fry eggs in a pan with olive oil until whites are set but yolks are runny", "Place eggs on top of the avocado toast", "Season with salt and pepper"]', 15, 2, 'EASY', 0),
('Floral Green Hummus', 'A creamy and delicious hummus made with chickpeas, tahini, and garlic.', 'uploads/recipes/floral-hummus.webp', 1, 'VEGAN', '["Drain and rinse the chickpeas", "In a food processor, combine chickpeas, tahini, minced garlic, and lemon juice", "Add salt and olive oil", "Blend until smooth, adding water if needed for desired consistency", "Taste and adjust seasonings as needed", "Transfer to a serving bowl and drizzle with olive oil"]', 15, 6, 'EASY', 0),
('Greek Salad', 'A refreshing Mediterranean salad with fresh vegetables and feta cheese.', 'uploads/recipes/greek-salad.webp', 2, 'HEALTHY', '["Chop cucumber, tomatoes, and red onion into bite-sized pieces", "Combine vegetables in a large bowl", "Add crumbled feta cheese and kalamata olives", "Drizzle with olive oil and red wine vinegar", "Season with salt and pepper", "Toss gently to combine"]', 15, 4, 'EASY', 0),
('Chocolate Mousse', 'A rich and creamy chocolate dessert that melts in your mouth.', 'uploads/recipes/chocolate-mousse.webp', 3, 'DESSERT', '["Melt dark chocolate in a double boiler", "Separate eggs and beat yolks with sugar", "Fold melted chocolate into egg yolks", "Whip egg whites until stiff peaks form", "Gently fold egg whites into chocolate mixture", "Refrigerate for at least 2 hours"]', 30, 4, 'MEDIUM', 0),
('Sushi Roll', 'Fresh and delicious homemade sushi with salmon and avocado.', 'uploads/recipes/sushi-roll.webp', 3, 'HEALTHY', '["Cook sushi rice according to package instructions", "Place nori sheet on bamboo mat", "Spread rice evenly on nori", "Add salmon and avocado slices", "Roll tightly using bamboo mat", "Slice into pieces and serve with soy sauce"]', 45, 2, 'HARD', 0);

-- Insert recipe ingredients
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES
-- Avocado Toast ingredients
(1, 1, 1, 'PIECE'),
(1, 2, 2, 'PIECE'),
(1, 4, 1, 'PINCH'),
(1, 5, 1, 'PINCH'),
(1, 6, 1, 'TABLESPOON'),
(1, 7, 1, 'PIECE'),
-- Avocado Egg Toast ingredients
(2, 1, 1, 'PIECE'),
(2, 2, 2, 'PIECE'),
(2, 3, 2, 'PIECE'),
(2, 4, 1, 'PINCH'),
(2, 5, 1, 'PINCH'),
(2, 6, 1, 'TABLESPOON'),
(2, 7, 1, 'PIECE'),
-- Hummus ingredients
(3, 8, 5, 'GRAM'),
(3, 9, 2, 'TABLESPOON'),
(3, 10, 2, 'PIECE'),
(3, 7, 1, 'PIECE'),
(3, 4, 1, 'PINCH'),
(3, 6, 2, 'TABLESPOON'),
-- Greek Salad ingredients
(4, 39, 1, 'PIECE'),
(4, 38, 2, 'PIECE'),
(4, 37, 1, 'PIECE'),
(4, 40, 100, 'GRAM'),
(4, 41, 50, 'GRAM'),
(4, 6, 2, 'TABLESPOON'),
(4, 42, 1, 'TABLESPOON'),
-- Chocolate Mousse ingredients
(5, 32, 200, 'GRAM'),
(5, 3, 4, 'PIECE'),
(5, 15, 2, 'TABLESPOON'),
(5, 14, 1, 'TABLESPOON'),
-- Sushi Roll ingredients
(6, 49, 2, 'CUP'),
(6, 48, 2, 'PIECE'),
(6, 50, 200, 'GRAM'),
(6, 1, 1, 'PIECE'),
(6, 45, 2, 'TABLESPOON');

-- Insert recipe likes
INSERT INTO recipe_likes (recipe_id, user_id) VALUES
(1, 1), -- silvia likes Avocado Toast
(1, 2), -- midudev likes Avocado Toast
(1, 3), -- david likes Avocado Toast (for recommendations)
(2, 1), -- silvia likes Avocado Egg Toast
(3, 1), -- silvia likes Hummus
(4, 2), -- midudev likes Greek Salad
(5, 3), -- david likes Chocolate Mousse
(6, 3); -- david likes Sushi Roll

-- Insert user preferred ingredients
INSERT INTO user_preferred_ingredients (user_id, ingredient_id) VALUES
-- Silvia's preferences (Mediterranean and healthy)
(1, 1),  -- Avocado
(1, 8),  -- Chickpeas
(1, 9),  -- Tahini
(1, 6),  -- Olive Oil
(1, 7),  -- Lemon
-- Midudev's preferences (Healthy and quick meals)
(2, 1),  -- Avocado
(2, 2),  -- Whole Grain Bread
(2, 6),  -- Olive Oil
(2, 39), -- Cucumber
(2, 38), -- Tomato
-- David's preferences (Healthy and desserts)
(3, 1),  -- Avocado
(3, 32), -- Dark Chocolate
(3, 31), -- Cocoa Powder
(3, 50), -- Salmon
(3, 48); -- Nori 