-- Insert users (passwords are encoded 'password')
INSERT INTO users (first_name, last_name, username, email, password, role) VALUES
('Silvia', 'García', 'mama', 'silvia@gmail.com', '$2a$10$K653U/97YyWvW1qHOWjMEOhN3viVukrEvbzWAxziWuYKyZrP/FWCi', 'USER'),
('Miguel A.', 'Durán', 'midudev', 'midudev@mail.com', '$2a$10$VsM6tV3zSZrBF134IhUEVui/h7MXF14PemlUFyTudASaumoLinfZe', 'USER'),
('David', 'Santos', 'david', 'dqvid01@gmail.com', '$2a$10$7MROLIKj/Pvr6q1yqnLyp.6uYom9w7Cv/CINcMn3EdR5x49YZ.F3q', 'USER'),
('Admin', '', 'admin', 'admin@mail.com', '$2a$10$rk4FI7tjSqJlpii9exYuA./vRzkcSsaCsaB9bxYxI9sCzXY/zCoSK', 'ADMIN');

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
('Peanut Butter');

-- Insert recipes
INSERT INTO recipes (name, description, image_path, author_id, label, steps, cook_time, servings, difficulty) VALUES
('Avocado Toast', 'A simple and delicious breakfast option with creamy avocado on toasted bread.', 'uploads/recipes/avocado-toast.webp', 1, 'HEALTHY', '["Toast the bread slices until golden and crispy", "Mash the avocado with a fork and mix with lemon juice, salt, and pepper", "Spread the mashed avocado on the toast", "Drizzle with olive oil and sprinkle with salt and pepper"]', 10, 2, 'EASY'),
('Avocado Egg Toast', 'Creamy avocado toast topped with perfectly fried eggs - a protein-packed breakfast upgrade.', 'uploads/recipes/avocado-egg-toast.webp', 2, 'HEALTHY', '["Toast the bread slices until golden and crispy", "Mash the avocado with lemon juice, salt, and pepper", "Spread the mashed avocado on the toast", "Fry eggs in a pan with olive oil until whites are set but yolks are runny", "Place eggs on top of the avocado toast", "Season with salt and pepper"]', 15, 2, 'EASY'),
('Floral Green Hummus', 'A creamy and delicious hummus made with chickpeas, tahini, and garlic.', 'uploads/recipes/floral-hummus.webp', 1, 'VEGAN', '["Drain and rinse the chickpeas", "In a food processor, combine chickpeas, tahini, minced garlic, and lemon juice", "Add salt and olive oil", "Blend until smooth, adding water if needed for desired consistency", "Taste and adjust seasonings as needed", "Transfer to a serving bowl and drizzle with olive oil"]', 15, 6, 'EASY');

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
(3, 6, 2, 'TABLESPOON');

-- Insert recipe likes
INSERT INTO recipe_likes (recipe_id, user_id) VALUES
(1, 1), -- silvia likes Avocado Toast
(1, 2), -- midudev likes Avocado Toast
(2, 1), -- silvia likes Avocado Egg Toast
(3, 1); -- silvia likes Hummus 