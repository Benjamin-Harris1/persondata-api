package edu.persondata.api.service;

import edu.persondata.api.dto.GenderizeResponse;
import edu.persondata.api.model.Person;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class GenderService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(GenderService.class);

    public GenderService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "genders" , key = "#person.firstName")
    public Mono<GenderizeResponse> getGender(Person person, String countryCode){
        logger.info("Fetching gender data for name: {} with country code: {}", person.getFirstName(), countryCode);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.genderize.io")
                        .path("/")
                        .queryParam("name", person.getFirstName())
                        .queryParamIfPresent("country_id", Optional.ofNullable(countryCode))
                        .build())
                .retrieve()
                .bodyToMono(GenderizeResponse.class)
                .doOnSuccess(response -> logger.info("Gender data fetched successfully"))
                .doOnError(error -> logger.error("Error fetching gender data for {}: {}", person.getFirstName(), error.getMessage()));
    }
}
