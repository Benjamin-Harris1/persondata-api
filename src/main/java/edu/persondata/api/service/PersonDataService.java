package edu.persondata.api.service;

import edu.persondata.api.dto.*;
import edu.persondata.api.model.Person;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Comparator;

@Service
public class PersonDataService {
    private final NationService nationService;
    private final GenderService genderService;
    private final AgeService ageService;

    public PersonDataService(NationService nationService, GenderService genderService, AgeService ageService) {
        this.nationService = nationService;
        this.genderService = genderService;
        this.ageService = ageService;
    }

    public Mono<CombinedResponse> getCombinedPersonData(String firstName, String middleName, String lastName) {
        Person person = new Person(firstName, middleName, lastName);

        // First, get the nationality using the full name
        Mono<NationalizeResponse> nationalityMono = nationService.getNationality(person);
    
        return nationalityMono.flatMap(nationality -> {
            CountryProbability mostProbableCountry = nationality.getCountry().stream()
                .max(Comparator.comparingDouble(CountryProbability::getProbability))
                .orElse(null);
    
            String countryCode = mostProbableCountry != null ? mostProbableCountry.getCountry_id() : "Unknown";
    
            // Then, use the country code to get gender and age
            Mono<GenderizeResponse> genderMono = genderService.getGender(person, countryCode);
            Mono<AgifyResponse> ageMono = ageService.getAge(person, countryCode);
    
            return Mono.zip(genderMono, ageMono).map(tuple -> {
                GenderizeResponse gender = tuple.getT1();
                AgifyResponse age = tuple.getT2();
    
                CombinedResponse response = new CombinedResponse();
                response.setFullName(person.getFullName());
                response.setFirstName(person.getFirstName());
                response.setMiddleName(person.getMiddleName());
                response.setLastName(person.getLastName());
                response.setGender(gender.getGender());
                response.setGenderProbability(gender.getProbability());
                response.setAge(age.getAge());
                response.setAgeProbability(1.0);  // Default value since no probability is provided
                response.setCountry(countryCode);
                response.setCountryProbability(mostProbableCountry != null ? mostProbableCountry.getProbability() : 0.0);
                return response;
            });
        });
    }
}
