package ru.skillbox.specification;

import lombok.Getter;

@Getter
public enum PersonSpecificationRoot {
    EMAIL("email"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    BIRTH_DATE("birthDate"),
    IS_ENABLED("isEnabled"),
    COUNTRY("country"),
    CITY("city");
    private final String value;

    PersonSpecificationRoot(String value) {
        this.value = value;
    }
}
