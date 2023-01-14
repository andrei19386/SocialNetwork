package ru.skillbox.exception;

public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException() {
        super("Пользователь не авторизован");
    }
}