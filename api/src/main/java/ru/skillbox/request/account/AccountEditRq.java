package ru.skillbox.request.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AccountEditRq {

    private String phone;

    private String about;

    private String city;

    private String country;

    private String firstName;

    private String lastName;

    private String birthDate;

    private String photoId;

    private String photoName;

}
