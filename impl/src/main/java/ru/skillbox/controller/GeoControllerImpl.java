package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.common.CityDto;
import ru.skillbox.common.CountryDto;
import ru.skillbox.service.GeoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GeoControllerImpl {
    private final GeoService geoService;

    @GetMapping("/api/v1/geo/countries")
    public ResponseEntity<List<CountryDto>> getCountries() {
        return geoService.getCountries();
    }


    @GetMapping("api/v1/geo/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCities(@PathVariable long countryId) {
        return geoService.getCities(countryId);
    }
}
