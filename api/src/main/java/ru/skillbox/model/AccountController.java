package ru.skillbox.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.request.account.AccountEditRq;
import ru.skillbox.response.Responsable;

@RequestMapping("api/v1/account")
public interface AccountController {

    ResponseEntity<Responsable> getCurrentPerson() throws JsonProcessingException;

    ResponseEntity<Responsable> editingAccount(@RequestBody AccountEditRq accountEditRq);

    ResponseEntity<Responsable> deleteAccount();

    ResponseEntity<Responsable> accountBlockingById(long id);

    ResponseEntity<Responsable> accountUnblockingById(long id);

    ResponseEntity<Responsable> getUserById(long id);

}
