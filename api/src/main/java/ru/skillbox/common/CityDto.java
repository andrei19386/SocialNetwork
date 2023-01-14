package ru.skillbox.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

    private Long id;
    private String title;
    private Integer countryId;
}
