ALTER TABLE ticket ADD COLUMN device_id INT NOT NULL, ADD COLUMN user_id INT NOT NULL;

ALTER TABLE ticket ADD CONSTRAINT fk_ticket_device FOREIGN KEY (device_id) REFERENCES devices (id),
    ADD CONSTRAINT fk_ticket_user FOREIGN KEY (user_id) REFERENCES users(id);
