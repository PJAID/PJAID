CREATE TABLE ticket_queue (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       ticket_id INT,
                       queue_status VARCHAR(255),
                       is_urgent TINYINT(1),
                       queued_at DATETIME,
                       assigned_technician_id BIGINT,

                       CONSTRAINT fk_ticket
                           FOREIGN KEY (ticket_id)
                               REFERENCES ticket(id)
                               ON DELETE SET NULL
);
