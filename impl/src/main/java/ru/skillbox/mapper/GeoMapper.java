package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skillbox.common.CityDto;
import ru.skillbox.common.CountryDto;
import ru.skillbox.model.City;
import ru.skillbox.model.Country;

@Mapper
public interface GeoMapper {
    GeoMapper INSTANCE = Mappers.getMapper(GeoMapper.class);

    @Mapping(source = "country", target = "countryId", qualifiedByName = "mapCountry")
    CityDto cityToCityDto(City city);


    CountryDto countryToCountryDto(Country country);

    @Named("mapCountry")
    default Long mapCountry(Country country) {
        return country.getId();
    }
}
