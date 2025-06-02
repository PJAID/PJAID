CREATE TABLE incident (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255),
                          priority VARCHAR(255),
                          technician_id INT,
                          CONSTRAINT fk_incident_technician
                              FOREIGN KEY (technician_id)
                                  REFERENCES users(id)
                                  ON DELETE SET NULL
);
