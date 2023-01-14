package ru.skillbox.model;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.response.Responsable;
import ru.skillbox.response.SearchResponse;

@RequestMapping("/api/v1/account")
public interface SearchController {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются поля timestamp, data, accessToken и tokenType)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SearchResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос (возвращаются поля error и error_description)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SearchResponse.class))
                    }
            )
    })
    @GetMapping("/search")
    ResponseEntity<Responsable> search(@RequestParam(required = false) String author,
                                       @RequestParam(required = false) String firstName,
                                       @RequestParam(required = false) String lastName,
                                       @RequestParam(required = false) Integer ageFrom,
                                       @RequestParam(required = false) Integer ageTo,
                                       @RequestParam(required = false) String city,
                                       @RequestParam(required = false) String country,
                                       @RequestParam(required = false, defaultValue = "20") Integer size);

}

