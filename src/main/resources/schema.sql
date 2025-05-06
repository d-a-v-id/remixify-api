-- Drop existing tables if they exist
DROP TABLE IF EXISTS user_preferred_ingredients;
DROP TABLE IF EXISTS recipe_likes;
DROP TABLE IF EXISTS recipe_ingredients;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at BIGINT NOT NULL,
    avatar_path VARCHAR(255) DEFAULT 'uploads/avatars/default-avatar.webp'
);

-- Create ingredients table
CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Create recipes table
CREATE TABLE recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    image_path VARCHAR(255) DEFAULT 'uploads/recipes/default-recipe.webp',
    author_id BIGINT NOT NULL,
    label VARCHAR(20),
    steps TEXT,
    cook_time BIGINT,
    servings INT,
    difficulty VARCHAR(20),
    likes BIGINT DEFAULT 0,
    FOREIGN KEY (author_id) REFERENCES users(id)
);

-- Create recipe_ingredients table (junction table)
CREATE TABLE recipe_ingredients (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    unit VARCHAR(20) NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

-- Create recipe_likes table (junction table)
CREATE TABLE recipe_likes (
    recipe_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (recipe_id, user_id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create user_preferred_ingredients table (junction table)
CREATE TABLE user_preferred_ingredients (
    user_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, ingredient_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);
