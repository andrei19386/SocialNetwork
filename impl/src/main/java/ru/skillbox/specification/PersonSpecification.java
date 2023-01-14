package ru.skillbox.specification;


import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.model.Person;

public class PersonSpecification {
    public static Specification<Person> getUsersByFirstName(String firstName) {
        if (firstName == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(PersonSpecificationRoot.FIRST_NAME.getValue())), getCorrectString(firstName));
    }

    public static Specification<Person> getUsersByLastName(String lastName) {
        if (lastName == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(PersonSpecificationRoot.LAST_NAME.getValue())), getCorrectString(lastName));
    }

    public static Specification<Person> getUsersByAgeFrom(Long ageFrom) {
        if (ageFrom == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(PersonSpecificationRoot.BIRTH_DATE.getValue()), ageFrom);
    }

    public static Specification<Person> getUsersByAgeTo(Long ageTo) {
        if (ageTo == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(PersonSpecificationRoot.BIRTH_DATE.getValue()), ageTo);
    }

    public static Specification<Person> getUsersByEnable() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PersonSpecificationRoot.IS_ENABLED.getValue()), true);
    }

    public static Specification<Person> skipCurrentPerson(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(PersonSpecificationRoot.EMAIL.getValue()), email);
    }

    public static Specification<Person> getUsersByCity(Long id) {
        if (id == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PersonSpecificationRoot.CITY.getValue()), id);
    }

    public static Specification<Person> getUsersByCountry(Long id) {
        if (id == null) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PersonSpecificationRoot.COUNTRY.getValue()), id);
    }

    public static Specification<Person> getUsersByAuthor(String author) {
        if (author == null) return null;
        return getUsersByFirstName(author).or(getUsersByLastName(author));
    }

    public static Specification<Person> getUsersByAuthor(String name, String lastName) {
        return getUsersByFirstName(name).and(getUsersByLastName(lastName));
    }

    private static String getCorrectString(String s) {
        return "%" + s.trim() + "%";
    }
}
