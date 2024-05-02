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

    private double roundProbability(double value){
        return Math.floor(value * 100) / 100d;
    }

    public void setGenderProbability(double genderProbability) {
        this.genderProbability = roundProbability(genderProbability);
    }

    public void setCountryProbability(double countryProbability) {
        this.countryProbability = roundProbability(countryProbability);
    }
}
