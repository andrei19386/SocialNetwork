package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.mapper.AccountMapper;
import ru.skillbox.model.AccountController;
import ru.skillbox.request.account.AccountEditRq;
import ru.skillbox.response.ErrorResponse;
import ru.skillbox.response.Responsable;
import ru.skillbox.service.PersonService;
import ru.skillbox.service.UserService;

@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final PersonService personService;

    private final UserService userService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<Responsable> getCurrentPerson() {
        try {
            return ResponseEntity.ok(AccountMapper.INSTANCE.personToAccountDto(personService.getCurrentPerson()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    @PutMapping("/me")
    public ResponseEntity<Responsable> editingAccount(@RequestBody AccountEditRq accountEditRq) {
        try {
            return ResponseEntity.ok(personService.editing(accountEditRq));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    @DeleteMapping("/me")
    public ResponseEntity<Responsable> deleteAccount() {
        try {
            userService.deleteCurrentUser();
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }

    }

    @Override
    @PutMapping("/block/{id}")
    public ResponseEntity<Responsable> accountBlockingById(@PathVariable long id) {
        try {
            userService.isBlockUserById(id, true);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    @DeleteMapping("/block/{id}")
    public ResponseEntity<Responsable> accountUnblockingById(@PathVariable long id) {
        try {
            userService.isBlockUserById(id, false);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Responsable> getUserById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(AccountMapper.INSTANCE.personToAccountDto(personService.getPersonById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }
}
