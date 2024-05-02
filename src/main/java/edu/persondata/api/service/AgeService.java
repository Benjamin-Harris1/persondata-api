package edu.persondata.api.service;

import edu.persondata.api.dto.AgifyResponse;
import edu.persondata.api.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AgeService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(AgeService.class);

    public AgeService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "ages" , key = "#person.fullName")
    public Mono<AgifyResponse> getAge(Person person, String countryCode) {
        logger.info("Fetching age data for name: {} with country code: {}", person.getFullName(), countryCode);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.agify.io")
                        .path("/")
                        .queryParam("name", person.getFullName())
                        .queryParamIfPresent("country_id", Optional.ofNullable(countryCode))
                        .build())
                .retrieve()
                .bodyToMono(AgifyResponse.class)
                .doOnSuccess(response -> logger.info("Age data fetched successfully"))
                .doOnError(error -> logger.error("Error fetching age data for {}: {}", person.getFullName(), error.getMessage()));

    }
}
