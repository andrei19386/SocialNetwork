package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skillbox.common.AccountDto;
import ru.skillbox.model.City;
import ru.skillbox.model.Country;
import ru.skillbox.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "city", qualifiedByName = "mapCity")
    @Mapping(target = "country", qualifiedByName = "mapCountry")
    @Mapping(target = "isOnline", source = "lastOnlineTime", qualifiedByName = "mapOnline")
    AccountDto personToAccountDto(Person person);

    @Named("mapCountry")
    default String mapCountry(Country country) {
        if (country == null) {
            return null;
        }
        return country.getTitle();
    }

    @Named("mapCity")
    default String mapCity(City city) {
        if (city == null) {
            return null;
        }
        return city.getTitle();
    }

    default List<AccountDto> ListPersonToListAccountDto(List<Person> people) {
        List<AccountDto> accountDto = new ArrayList<>();
        people.forEach(person -> accountDto.add(personToAccountDto(person)));
        return accountDto;
    }

    default int getTime() {
        return 5 * 60 * 1000;
    }

    @Named("mapOnline")
    default boolean checkIsOnline(Long lastOnlineTime) {
        long ms = new Date().getTime() - lastOnlineTime;
        return ms < getTime();
    }
}
