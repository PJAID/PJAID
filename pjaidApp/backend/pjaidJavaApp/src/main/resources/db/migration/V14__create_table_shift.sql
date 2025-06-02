CREATE TABLE shifts (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        date DATE NOT NULL,
                        shift VARCHAR(255) NOT NULL,
                        hour_from VARCHAR(255),
                        hour_to VARCHAR(255),
                        duration_hours INT
);