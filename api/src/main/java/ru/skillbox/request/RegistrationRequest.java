package ru.skillbox.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String code;
    private String token;
}
