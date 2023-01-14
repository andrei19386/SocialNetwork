package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.enums.Status;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.request.MessageRq;
import ru.skillbox.mapper.DialogMapper;
import ru.skillbox.model.Dialog;
import ru.skillbox.model.Message;
import ru.skillbox.model.Person;
import ru.skillbox.repository.DialogRepository;
import ru.skillbox.repository.MessageRepository;
import ru.skillbox.response.*;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class DialogService {
    private final DialogRepository dialogRepository;
    private final MessageRepository messageRepository;
    private final DialogMapper dialogMapper;
    private final PersonService personService;

    //Получение списка диалогов пользователя
    @Transactional(readOnly = true)
    public ResponseEntity<Responsable> getDialogs(Integer offset, Integer itemPerPage) {
        Person currentPerson = personService.getCurrentPerson();
        List<Dialog> dialogList = dialogRepository.findAllDialogsForPerson(currentPerson);

        dialogList.forEach(dialog -> {
            dialog.setConversationPartner(dialog.getCompanion1().equals(currentPerson)
                    ? dialog.getCompanion2() : dialog.getCompanion1());

            dialog.setUnreadCount(
                    messageRepository.findAllByDialogId(dialog)
                            .stream()
                            .filter(message -> message.getRecipientId().getId().equals(currentPerson.getId()))
                            .filter(message -> message.getStatus().equals(Status.SENT))
                            .count());

            dialog.setLastMessage(
                    dialog.getMessages().stream()
                            .max(Comparator.comparing(Message::getTime))
                            .orElse(new Message()));
        });

        return ResponseEntity.ok(
                DialogListResponse.builder()
                        .total(10)
                        .offset(offset)
                        .perPage(itemPerPage)
                        .currentUserId(currentPerson.getId())
                        .data(dialogMapper.listDialogToDto(dialogList))
                        .build());
    }

    //Получение сообщений диалога
    @Transactional
    public ResponseEntity<Responsable> getMessages(Long id, Integer offset, Integer itemPerPage) {
        Person currentPerson = personService.getCurrentPerson();
        Person conversationPartner;
        try {
            conversationPartner = personService.getPersonById(id);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        Optional<Dialog> dialog = dialogRepository.findDialogByCompanions(
                currentPerson, conversationPartner);

        Dialog dialogRes = dialog.orElseGet(() -> saveAndGetDialog(currentPerson, conversationPartner));

        return ResponseEntity.ok(
                DialogRs.builder()
                        .total(10)
                        .offset(offset)
                        .perPage(itemPerPage)
                        .data(dialogMapper.listMessageToDto(messageRepository.findAllByDialogId(dialogRes)))
                        .build());
    }

    //Сохраниние сообщения
    @Transactional
    public void saveMessage(MessageRq messageRq) {
        Message message = dialogMapper.DtoToMessage(messageRq.getData());
        message.setStatus(Status.SENT);
        Optional<Dialog> dialog = dialogRepository.findDialogByCompanions(message.getAuthorId(),
                message.getRecipientId());
        message.setDialogId(
                dialog.orElseGet(() ->
                        saveAndGetDialog(message.getAuthorId(), message.getRecipientId())));
        messageRepository.save(message);
    }

    //Сохранение диалога двух пользователей
    private Dialog saveAndGetDialog(Person companion1, Person companion2) {
        Dialog dialog = new Dialog();
        dialog.setCompanion1(companion1);
        dialog.setCompanion2(companion2);

        return dialogRepository.save(dialog);
    }

    //Пометить сообщения прочитанными
    @Transactional
    public ResponseEntity<Responsable> markAsRead(Long id) {
        Person currentPerson = personService.getCurrentPerson();

        try {
            Dialog dialog = dialogRepository.findDialogByCompanions(
                    currentPerson, personService.getPersonById(id)).get();

            List<Message> readMessage = new ArrayList<>();

            messageRepository.findAllByDialogId(dialog).forEach(message -> {
                if (message.getRecipientId().getId().equals(currentPerson.getId())) {
                    message.setStatus(Status.READ);
                    readMessage.add(message);
                }
            });

            messageRepository.saveAll(readMessage);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(
                MessageRs.builder()
                        .data(DataMessage.builder()
                                .message("Ok")
                                .build())
                        .build());
    }


    //Посчитать количество непрочитанных сообщений
    @Transactional(readOnly = true)
    public ResponseEntity<Responsable> getUnreadMessage() {

        List<Message> unreadMessages = messageRepository.findAllByRecipientIdAndStatus(
                personService.getCurrentPerson(), Status.SENT);

        return ResponseEntity.ok(
                MessageRs.builder()
                        .data(DataMessage.builder()
                                .count(unreadMessages.size())
                                .build())
                        .build());
    }
}


