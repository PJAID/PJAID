package org.api.pjaidapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Coordinate {
    private double longitude;
    private double latitude;
    @Column(name = "top_order")
    private int topOrder;
}

