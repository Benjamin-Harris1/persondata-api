package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NationalizeResponse {
    private String name;
    private List<CountryProbability> country;
}
