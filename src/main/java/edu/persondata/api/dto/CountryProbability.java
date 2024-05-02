package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryProbability {
    private String countryId;
    private double probability;
}
