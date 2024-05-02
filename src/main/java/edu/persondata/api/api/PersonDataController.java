package edu.persondata.api.api;

import edu.persondata.api.dto.GenderizeResponse;
import edu.persondata.api.service.PersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PersonDataController {

    @Autowired
    private PersonDataService personDataService;

    @GetMapping("/gender")
    public Mono<GenderizeResponse> getGender(@RequestParam String name) {
        return personDataService.getGender(name);
    }
}
