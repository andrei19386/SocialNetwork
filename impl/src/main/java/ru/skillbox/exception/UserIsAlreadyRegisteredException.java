package ru.skillbox.exception;

public class UserIsAlreadyRegisteredException extends RuntimeException{
    public UserIsAlreadyRegisteredException() {
        super("Этот аккуант уже зарегистрирован");
    }
}
