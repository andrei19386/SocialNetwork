package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.common.AccountDto;
import ru.skillbox.exception.NotAuthorizedException;
import ru.skillbox.mapper.AccountMapper;
import ru.skillbox.model.Person;
import ru.skillbox.repository.PersonRepository;
import ru.skillbox.request.SearchRequest;
import ru.skillbox.response.SearchResponse;
import ru.skillbox.specification.PersonSpecification;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PersonRepository personRepository;
    private final PersonService personService;

    private final GeoService geoService;

    public SearchResponse search(SearchRequest request,
                                 Integer size) throws NotAuthorizedException {
        Person person = personService.getCurrentPerson();
        Page<Person> people = searchFilter(specification(request, person), size);
        return new SearchResponse().getOkResponse(AccountMapper.INSTANCE.ListPersonToListAccountDto(people.getContent()), people);
    }

    private Page<Person> searchFilter(Specification<Person> specification, int size) {
        return personRepository.findAll(specification, Pageable.ofSize(size));
    }

    private Specification<Person> specification(SearchRequest request, Person current) {
        return Specification
                .where(specificationAuthor(request.getAuthor()))
                .and(personByEnableAndSkipCurrent(current))
                .and(PersonSpecification.getUsersByFirstName(request.getFirstName()))
                .and(PersonSpecification.getUsersByLastName(request.getLastName()))
                .and(PersonSpecification.getUsersByAgeFrom(yearToTimeInMillis(request.getAgeFrom())))
                .and(PersonSpecification.getUsersByAgeTo(yearToTimeInMillis(request.getAgeTo())))
                .and(PersonSpecification.getUsersByCity(getIdCityByTitle(request.getCity())))
                .and(PersonSpecification.getUsersByCountry(getIdCountryByTitle(request.getCountry())));
    }

    private Specification<Person> specificationAuthor(String author) {
        if (author == null) return null;
        List<String> strings = Arrays.asList(author.replaceAll("\\s+", " ").split(" "));
        switch (strings.size()) {
            case 1:
                return PersonSpecification.getUsersByAuthor(strings.get(0));
            case 2:
                return PersonSpecification.getUsersByAuthor(strings.get(0), strings.get(1))
                        .or(PersonSpecification.getUsersByAuthor(strings.get(1), strings.get(0)));
        }
        return PersonSpecification.getUsersByAuthor(author);
    }

    private Long yearToTimeInMillis(Integer year) {
        if (year == null) return null;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR) - year,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
        return calendar.getTimeInMillis();
    }

    private Long getIdCountryByTitle(String title) {
        if (title == null) return null;
        return geoService.getCountryByTitle(title).getId();
    }

    private Long getIdCityByTitle(String title) {
        if (title == null) return null;
        return geoService.getCityByTitle(title).getId();
    }

    public List<AccountDto> searchRecommendations() throws NotAuthorizedException {
        Person person = personService.getCurrentPerson();
        Page<Person> people = searchFilter(specificationRecommendations(person), 5);
        return AccountMapper.INSTANCE.ListPersonToListAccountDto(people.getContent());
    }

    private Specification<Person> specificationRecommendations(Person current) {
        return Specification
                .where(personByEnableAndSkipCurrent(current)
                        .and(Specification.where(PersonSpecification.getUsersByCity(current.getCity() == null ? null : current.getCity().getId()))
                                .or(PersonSpecification.getUsersByCountry(current.getCountry() == null ? null : current.getCountry().getId()))))
                .or(personByEnableAndSkipCurrent(current))
                ;
    }

    private Specification<Person> personByEnableAndSkipCurrent(Person current) {
        return Specification.where(PersonSpecification.getUsersByEnable())
                .and(PersonSpecification.skipCurrentPerson(current.getEmail()));
    }

}
