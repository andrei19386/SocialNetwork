package ru.skillbox.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.MessagePermission;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.response.Responsable;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto implements Responsable {
    private Long id;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private String city;
    private String country;
    private String firstName;
    private String lastName;
    private Long regDate;
    private Long birthDate;
    private MessagePermission messagePermission;
    private Long lastOnlineTime;
    private Boolean isOnline;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private StatusCode statusCode;

    @Override
    public Responsable getResponse(String s) {
        return null;
    }
}

