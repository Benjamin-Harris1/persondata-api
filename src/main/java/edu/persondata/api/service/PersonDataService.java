package edu.persondata.api.service;

import edu.persondata.api.dto.GenderizeResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
