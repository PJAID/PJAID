CREATE TABLE devices (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        serial_number VARCHAR(255),
                        purchase_date VARCHAR(255),
                        last_service VARCHAR(255)
);