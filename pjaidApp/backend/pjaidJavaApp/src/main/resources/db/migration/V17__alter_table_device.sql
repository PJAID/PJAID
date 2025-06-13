ALTER TABLE ticket
    ADD COLUMN priority VARCHAR(50),
    ADD COLUMN latitude DECIMAL(10,8),
    ADD COLUMN longitude DECIMAL(11,8);
