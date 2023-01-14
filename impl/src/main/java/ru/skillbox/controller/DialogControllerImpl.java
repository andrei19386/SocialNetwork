package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.model.DialogController;
import ru.skillbox.response.*;
import ru.skillbox.service.DialogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dialogs")
public class DialogControllerImpl implements DialogController {

    private final DialogService dialogService;

    //Получение списка диалогов пользователя
    @GetMapping
    public ResponseEntity<Responsable> allDialogs(@RequestParam(required = false) Integer offset,
                                                  @RequestParam(required = false) Integer itemPerPage) {
        try {
            return dialogService.getDialogs(offset, itemPerPage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    //Получение сообщений диалога
    @GetMapping("/messages")
    public ResponseEntity<Responsable> getMessages(@RequestParam Long companionId,
                                                   @RequestParam(required = false) Integer offset,
                                                   @RequestParam(required = false) Integer itemPerPage) {
        try {
            return dialogService.getMessages(companionId, offset, itemPerPage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }

    }

    //Пометить сообщения прочитанными
    @PutMapping("/{companionId}")
    public ResponseEntity<Responsable> markAsRead(@PathVariable Long companionId) {
        try {
            return dialogService.markAsRead(companionId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    //Получить количество непрочитанных сообщений
    @GetMapping("/unreaded")
    public ResponseEntity<Responsable> getUnreadMessages() {
        try {
            return dialogService.getUnreadMessage();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }
}

