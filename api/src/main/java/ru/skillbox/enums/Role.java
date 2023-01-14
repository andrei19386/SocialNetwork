package ru.skillbox.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ANONYMOUS("ROLE_ANONYMOUS");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
