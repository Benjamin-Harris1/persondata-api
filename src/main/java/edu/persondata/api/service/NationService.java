package edu.persondata.api.service;

import edu.persondata.api.dto.NationalizeResponse;
import edu.persondata.api.model.Person;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NationService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(NationService.class);

    public NationService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable(value = "nationalities", key = "#person.fullName")
    public Mono<NationalizeResponse> getNationality(Person person) {
        logger.info("Fetching nationality data for name: {}", person.getFullName());
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.scheme("https")
                        .host("api.nationalize.io")
                        .path("/")
                        .queryParam("name", person.getFullName())
                        .build())
                .retrieve()
                .bodyToMono(NationalizeResponse.class)
                .doOnSuccess(response -> logger.info("Nationality data fetched successfully"))
                .doOnError(error -> logger.error("Error fetching nationality data for {}: {}", person.getFullName(), error.getMessage()));
    }
}
