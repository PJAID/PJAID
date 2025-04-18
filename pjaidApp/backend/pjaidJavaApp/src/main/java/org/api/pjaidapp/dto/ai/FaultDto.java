package org.api.pjaidapp.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FaultDto {

    @JsonProperty("Numer IT")
    private String numerIt;

    @JsonProperty("Liczba Napraw")
    private double liczbaNapraw;

    @JsonProperty("rekonstrukcja_mse")
    private double rekonstrukcjaMse;

    @JsonProperty("ITNumber")
    private String itNumber;

    @JsonProperty("Model")
    private String model;
}