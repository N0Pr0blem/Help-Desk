CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(32) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    second_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    is_active BOOLEAN NOT NULL,
    role VARCHAR(16) NOT NULL
);