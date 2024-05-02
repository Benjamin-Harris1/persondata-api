package edu.persondata.api.api;


import edu.persondata.api.dto.CombinedResponse;
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

    @GetMapping("/persondata")
    public Mono<CombinedResponse> getPersonData(@RequestParam String firstName, @RequestParam(required = false) String middleName, @RequestParam String lastName) {
        return personDataService.getCombinedPersonData(firstName, middleName, lastName);
    }

}
