package ru.skillbox.exception;
//todo удалить если не используется

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Неверные учётнные данные");
    }
}
