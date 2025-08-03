CREATE TABLE halls
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE hall_coordinates
(
    hall_id BIGINT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    top_order INT,
    PRIMARY KEY (hall_id, latitude, longitude),
    CONSTRAINT fk_hall_coordinates_hall FOREIGN KEY (hall_id)
        REFERENCES halls (id)
        ON DELETE CASCADE
);

INSERT INTO halls (name)
VALUES ('B1');
INSERT INTO halls (name)
VALUES ('B2');
INSERT INTO halls (name)
VALUES ('B3');
INSERT INTO halls (name)
VALUES ('B4');
INSERT INTO halls (name)
VALUES ('B5');

-- B1
INSERT INTO hall_coordinates (hall_id, latitude, longitude, top_order)
VALUES ((SELECT id FROM halls WHERE name = 'B1'), 54.11106253950224, 18.79792476056409, 1),
       ((SELECT id FROM halls WHERE name = 'B1'), 54.11163865401103, 18.800307105633082, 2),
       ((SELECT id FROM halls WHERE name = 'B1'), 54.11288679532512, 18.79948981991306, 3),
       ((SELECT id FROM halls WHERE name = 'B1'), 54.11232250950628, 18.7970717542607, 4);

-- B2
INSERT INTO hall_coordinates (hall_id, latitude, longitude, top_order)
VALUES ((SELECT id FROM halls WHERE name = 'B2'), 54.11047320604692, 18.79574864440839, 1),
       ((SELECT id FROM halls WHERE name = 'B2'), 54.11093712940937, 18.79787025247736, 2),
       ((SELECT id FROM halls WHERE name = 'B2'), 54.11230453664569, 18.79693303343161, 3),
       ((SELECT id FROM halls WHERE name = 'B2'), 54.11181368839292, 18.79480937575306, 4);

-- B3
INSERT INTO hall_coordinates (hall_id, latitude, longitude, top_order)
VALUES ((SELECT id FROM halls WHERE name = 'B3'), 54.11002221754456, 18.79376913584779, 1),
       ((SELECT id FROM halls WHERE name = 'B3'), 54.11042151536588, 18.79544992580022, 2),
       ((SELECT id FROM halls WHERE name = 'B3'), 54.11156348729286, 18.79464536085406, 3),
       ((SELECT id FROM halls WHERE name = 'B3'), 54.11115886087713, 18.7929793885563, 4);

-- B4
INSERT INTO hall_coordinates (hall_id, latitude, longitude, top_order)
VALUES ((SELECT id FROM halls WHERE name = 'B4'), 54.10965154326212, 18.79190464715909, 1),
       ((SELECT id FROM halls WHERE name = 'B4'), 54.10991135060899, 18.79297560017192, 2),
       ((SELECT id FROM halls WHERE name = 'B4'), 54.11105370206089, 18.79218924682722, 3),
       ((SELECT id FROM halls WHERE name = 'B4'), 54.11081206637366, 18.79112424733728, 4);

-- B5
INSERT INTO hall_coordinates (hall_id, latitude, longitude, top_order)
VALUES ((SELECT id FROM halls WHERE name = 'B5'), 54.10914818190224, 18.78980642705848, 1),
       ((SELECT id FROM halls WHERE name = 'B5'), 54.10962555281818, 18.79186672854524, 2),
       ((SELECT id FROM halls WHERE name = 'B5'), 54.11081555867077, 18.79109370614359, 3),
       ((SELECT id FROM halls WHERE name = 'B5'), 54.11031906147654, 18.78902240387642, 4);

