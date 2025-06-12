package org.api.pjaidapp.model;

import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDate;

@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;  // kolumna A: DATA

    @Column(nullable = false)
    private String shift;    // kolumna B: zmiana

    @Column(name = "hour_from")
    private String hourFrom; // kolumna C: GODZINA_OD

    @Column(name = "hour_to")
    private String hourTo;   // kolumna D: GODZINA_DO

    @Column(name = "duration_hours")
    private int duration;    // kolumna E: CZAS
}

