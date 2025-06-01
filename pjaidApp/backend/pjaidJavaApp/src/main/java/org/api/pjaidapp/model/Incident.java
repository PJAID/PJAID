package org.api.pjaidapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.api.pjaidapp.enums.Priority;

@Getter
@Setter
@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User technician;

}
