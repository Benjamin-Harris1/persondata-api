package edu.persondata.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgifyResponse {
    private String name;
    private int age;
    private double probability;
}
