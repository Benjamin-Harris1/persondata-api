package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombinedResponse {
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private double genderProbability;
    private int age;
    private double ageProbability;
    private String country;
    private double countryProbability;
}
