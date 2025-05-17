ALTER TABLE users
    ADD COLUMN password VARCHAR(255) NOT NULL;

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INT NOT NULL,
                                          roles VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
