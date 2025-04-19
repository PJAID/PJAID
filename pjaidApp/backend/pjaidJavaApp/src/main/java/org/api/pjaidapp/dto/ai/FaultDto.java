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

    @JsonProperty("numerIt")
    private String numberIt;

    @JsonProperty("model")
    private String model;

    @JsonProperty("rekonstrukcjaMse")
    private double reconstructionMse;

    @JsonProperty("liczbaNapraw")
    private double repairNumber;
}