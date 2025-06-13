ALTER TABLE ticket DROP CHECK ticket_chk_1;

ALTER TABLE ticket
    ADD CONSTRAINT ticket_chk_1
        CHECK (
            status IN (
                       'NOWE',
                       'W_TRAKCIE',
                       'PRZESTOJ',
                       'ZAMKNIETE',
                       'OCZEKUJACE'
                )
            );
