package edu.persondata.api.service;

import edu.persondata.api.dto.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

@Service
public class PersonDataService {
    private final WebClient webClient = WebClient.create();
    private static final Logger logger = LoggerFactory.getLogger(PersonDataService.class);

    @Cacheable(value = "genders" , key = "#name")
    public Mono<GenderizeResponse> getGender(String name){
        logger.info("Fetching gender data");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.genderize.io")
                        .path("/")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToMono(GenderizeResponse.class)
                .doOnSuccess(response -> logger.info("Gender data fetched successfully"));
    }

    @Cacheable(value = "nationalities" , key = "#name")
    public Mono<NationalizeResponse> getNationality(String name){
        logger.info("Fetching nationality data");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.nationalize.io")
                        .path("/")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToMono(NationalizeResponse.class)
                .doOnSuccess(response -> logger.info("Nation data fetched successfully"));
    }

    @Cacheable(value = "ages" , key = "#name")
    public Mono<AgifyResponse> getAge(String name) {
        logger.info("Fetching age data");
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.agify.io")
                        .path("/")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .bodyToMono(AgifyResponse.class)
                .doOnSuccess(response -> logger.info("Age data fetched successfully"));
    }

    public Mono<CombinedResponse> getCombinedPersonData(String firstName, String middleName, String lastName) {
        String fullName = String.join(" ", firstName, middleName != null ? middleName : "", lastName).trim();
        Mono<GenderizeResponse> genderMono = getGender(firstName);
        Mono<NationalizeResponse> nationalityMono = getNationality(firstName);
        Mono<AgifyResponse> ageMono = getAge(firstName);

        return Mono.zip(genderMono, nationalityMono, ageMono)
                   .map(tuple -> {
                       GenderizeResponse gender = tuple.getT1();
                       NationalizeResponse nationality = tuple.getT2();
                       AgifyResponse age = tuple.getT3();
                       CombinedResponse response = new CombinedResponse();
                       response.setFullName(fullName);
                       response.setFirstName(firstName);
                       response.setMiddleName(middleName);
                       response.setLastName(lastName);
                       response.setGender(gender.getGender());
                       response.setGenderProbability(gender.getProbability());
                       response.setAge(age.getAge());
                       response.setAgeProbability(age.getProbability());
                       
                       // Select the most country with highest probability
                       CountryProbability mostProbableCountry = nationality.getCountry().stream()
                       .max(Comparator.comparingDouble(CountryProbability::getProbability))
                       .orElse(null);

                       if (mostProbableCountry != null) {
                        response.setCountry(mostProbableCountry.getCountryId());
                        response.setCountryProbability(mostProbableCountry.getProbability());
                       }

                       return response;
                   });
    }
}
