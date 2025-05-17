CREATE TABLE IF NOT EXISTS refresh_tokens (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              token VARCHAR(255) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    expiry_date DATETIME(6) NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    );
