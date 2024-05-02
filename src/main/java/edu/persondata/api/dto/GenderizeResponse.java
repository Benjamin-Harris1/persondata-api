package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenderizeResponse {
    private String name;
    private String gender;
    private double probability;

    
}
