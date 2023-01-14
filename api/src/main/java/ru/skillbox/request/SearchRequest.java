package ru.skillbox.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchRequest {
    String author;
    String firstName;
    String lastName;
    Integer ageFrom;
    Integer ageTo;
    String city;
    String country;
}
