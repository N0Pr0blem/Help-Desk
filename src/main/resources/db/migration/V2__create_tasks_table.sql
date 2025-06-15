CREATE TABLE task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    description TEXT,
    from_user_id BIGINT,
    to_user_id BIGINT,
    created_at TIMESTAMP,
    finished_at TIMESTAMP,
    status VARCHAR(50)
);