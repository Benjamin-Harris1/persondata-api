package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryProbability {
    private String country_id;
    private double probability;
}
