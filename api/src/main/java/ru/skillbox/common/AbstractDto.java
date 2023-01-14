package ru.skillbox.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractDto {

    private String message;

    public AbstractDto(String message) {
        this.message = message;
    }
}
