package ru.skillbox.response.settings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingsDto {

    Long id;
    Long userId;
    boolean friendRequest;
    boolean friendBirthday;
    boolean postComment;
    boolean commentComment;
    boolean post;
    boolean message;
    boolean sendPhoneMessage;
    boolean sendEmailMessage;

}