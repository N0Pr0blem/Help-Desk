CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(32) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(64),
    second_name VARCHAR(64),
    last_name VARCHAR(64),
    activation_code VARCHAR(64),
    is_active BOOLEAN NOT NULL,
    role VARCHAR(16) NOT NULL
);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    from_user_id BIGINT UNSIGNED NOT NULL,
    to_user_id BIGINT UNSIGNED,
    created_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY(from_user_id) REFERENCES users(id),
    FOREIGN KEY(to_user_id) REFERENCES users(id)
);