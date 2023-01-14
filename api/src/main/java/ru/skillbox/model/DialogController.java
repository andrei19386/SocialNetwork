package ru.skillbox.model;

import org.springframework.http.ResponseEntity;
import ru.skillbox.response.DialogListResponse;
import ru.skillbox.response.DialogRs;
import ru.skillbox.response.MessageRs;
import ru.skillbox.response.Responsable;

public interface DialogController {
    ResponseEntity<Responsable> allDialogs(Integer offset, Integer itemPerPage);

    ResponseEntity<Responsable> getMessages(Long interlocutorId, Integer offset, Integer itemPerPage);

    ResponseEntity<Responsable> markAsRead(Long companionId);

    ResponseEntity<Responsable> getUnreadMessages();
}
