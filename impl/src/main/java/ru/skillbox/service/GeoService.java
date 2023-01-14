package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.common.CityDto;
import ru.skillbox.common.CountryDto;
import ru.skillbox.mapper.GeoMapper;
import ru.skillbox.model.City;
import ru.skillbox.model.Country;
import ru.skillbox.repository.CityRepository;
import ru.skillbox.repository.CountryRepository;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class GeoService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public ResponseEntity<List<CountryDto>> getCountries() {
        List<Country> countryList = countryRepository.findAll();
        List<CountryDto> countryDtoList = new ArrayList<>();
        for (Country country : countryList) {
            CountryDto countryDto = GeoMapper.INSTANCE.countryToCountryDto(country);
            countryDtoList.add(countryDto);
        }
        return ResponseEntity.ok(countryDtoList);
    }

    public ResponseEntity<List<CityDto>> getCities(long countryId) {
        Country country = countryRepository.findById(countryId).get();
        List<City> cityList = country.getCities();
        List<CityDto> cityDtoList = new ArrayList<>();
        cityList.forEach(c -> cityDtoList.add(GeoMapper.INSTANCE.cityToCityDto(c)));
        return ResponseEntity.ok(cityDtoList);
    }

    public Country getCountryByTitle(String title) {
        return countryRepository.findByTitle(title);
    }

    public City getCityByTitle(String title) {
        return cityRepository.findByTitle(title);
    }
}
