package ru.skillbox.response.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto implements Serializable {
    private Long id;
    private String photo;
    private String statusCode;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private Long birthDate;
    private Boolean isOnline;
}
