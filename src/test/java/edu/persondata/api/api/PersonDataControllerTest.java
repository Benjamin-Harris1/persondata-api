package edu.persondata.api.api;

import edu.persondata.api.dto.CombinedResponse;
import edu.persondata.api.service.PersonDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebFluxTest(PersonDataController.class)
class PersonDataControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PersonDataService personDataService;

    @Test
    void getPersonData() {
        // Act
        String firstName = "Harry";
        String middleName = "James";
        String lastName = "Potter";
        CombinedResponse mockResponse = new CombinedResponse();
        mockResponse.setFullName("Harry James Potter");
        mockResponse.setFirstName(firstName);
        mockResponse.setMiddleName(middleName);
        mockResponse.setLastName(lastName);
        mockResponse.setGender("male");
        mockResponse.setGenderProbability(0.99);
        mockResponse.setAge(30);
        mockResponse.setAgeProbability(0.95);
        mockResponse.setCountry("GB");
        mockResponse.setCountryProbability(0.90);

        given(personDataService.getCombinedPersonData(firstName, middleName, lastName))
                .willReturn(Mono.just(mockResponse));

        // When & Then
        webTestClient.get().uri("/persondata?firstName=Harry&middleName=James&lastName=Potter")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fullName").isEqualTo("Harry James Potter")
                .jsonPath("$.firstName").isEqualTo("Harry")
                .jsonPath("$.middleName").isEqualTo("James")
                .jsonPath("$.lastName").isEqualTo("Potter")
                .jsonPath("$.gender").isEqualTo("male")
                .jsonPath("$.genderProbability").isEqualTo(0.99)
                .jsonPath("$.age").isEqualTo(30)
                .jsonPath("$.ageProbability").isEqualTo(0.95)
                .jsonPath("$.country").isEqualTo("GB")
                .jsonPath("$.countryProbability").isEqualTo(0.90);
    }

}
