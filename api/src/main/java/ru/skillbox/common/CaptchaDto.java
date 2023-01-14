package ru.skillbox.common;

import lombok.Data;

@Data
public class CaptchaDto {
    private String code;
    private byte[] bytes;

    public CaptchaDto(String code, byte[] bytes) {
        this.code = code;
        this.bytes = bytes;
    }
}
