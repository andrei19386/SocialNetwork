package ru.skillbox.common;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.skillbox.enums.StatusCode;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class SearchPersonDto {
    private List<Long> ids;
    private String firstName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime birthDateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime birthDateTo;
    private String city;
    private String country;
    private Integer ageTo;
    private Integer ageFrom;
    private StatusCode statusCode;
}
