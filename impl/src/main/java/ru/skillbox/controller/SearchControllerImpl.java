package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.SearchController;
import ru.skillbox.request.SearchRequest;
import ru.skillbox.response.ErrorResponse;
import ru.skillbox.response.Responsable;
import ru.skillbox.service.SearchService;

@RestController
@RequiredArgsConstructor
public class SearchControllerImpl implements SearchController {

    private final SearchService searchService;

    @Override
    public ResponseEntity<Responsable> search(String author, String firstName, String lastName, Integer ageFrom, Integer ageTo, String city, String country, Integer size) {
        try {
            SearchRequest request = SearchRequest.builder()
                    .author(author)
                    .firstName(firstName)
                    .lastName(lastName)
                    .ageTo(ageTo)
                    .ageFrom(ageFrom)
                    .city(city)
                    .country(country)
                    .build();
            return ResponseEntity.ok(searchService.search(request, size));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }
}
