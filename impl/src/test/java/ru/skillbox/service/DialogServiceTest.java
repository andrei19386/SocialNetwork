package ru.skillbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.skillbox.enums.Status;
import ru.skillbox.mapper.DialogMapper;
import ru.skillbox.model.Dialog;
import ru.skillbox.model.Message;
import ru.skillbox.model.Person;
import ru.skillbox.repository.DialogRepository;
import ru.skillbox.repository.MessageRepository;
import ru.skillbox.response.Responsable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
public class DialogServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private DialogRepository dialogRepository;

    @Mock
    private DialogMapper dialogMapper;

    @Mock
    private PersonService personService;

    @InjectMocks
    private DialogService dialogService;

    @Test
    @DisplayName("checkDialogs")
    public void getDialogsTest() {
        List<Person> people = getPeople();
        Mockito.when(personService.getCurrentPerson()).thenReturn(people.get(0));

        List<Dialog> dialogs = getDialogs();
        dialogs.get(0).setCompanion1(people.get(0));
        dialogs.get(0).setCompanion2(people.get(1));
        Mockito.when(dialogRepository.findAllDialogsForPerson(any())).thenReturn(dialogs);

        ResponseEntity<Responsable> result = dialogService.getDialogs(10, 10);

        assertNotNull(result);
        //assertEquals(getPeople().get(1).getId(), Objects.requireNonNull(result.getBody()).getData().get(0).getConversationPartner().getId());

    }

    @Test
    @DisplayName("checkUnreadMsgCount")
    public void getUnreadMessageTest(){

        Mockito.when(personService.getCurrentPerson()).thenReturn(getPeople().get(0));

        Mockito.when(messageRepository.findAllByRecipientIdAndStatus(any(), any())).thenReturn(
                getMessages().stream()
                        .filter(msg -> msg.getStatus().equals(Status.SENT))
                        .collect(Collectors.toList()));

        ResponseEntity<Responsable> result = dialogService.getUnreadMessage();

        Mockito.verify(messageRepository, atLeastOnce()).findAllByRecipientIdAndStatus(any(), any());

        assertNotNull(result);
        //assertEquals(1, Objects.requireNonNull(result.getBody()).getData().getCount());
    }

    private List<Message> getMessages() {
        Message message1 = new Message();
        Message message2 = new Message();
        Message message3 = new Message();

        message1.setId(1L);
        message1.setTime(LocalDateTime.now());
        message1.setStatus(Status.READ);
        message1.setMessageText("Привет");
        message1.setAuthorId(getPeople().get(0));
        message1.setRecipientId(getPeople().get(1));
        message1.setDialogId(getDialogs().get(0));

        message2.setId(2L);
        message2.setTime(LocalDateTime.now());
        message2.setStatus(Status.READ);
        message2.setMessageText("Йо");
        message2.setAuthorId(getPeople().get(1));
        message2.setRecipientId(getPeople().get(0));
        message2.setDialogId(getDialogs().get(0));

        message3.setId(3L);
        message3.setTime(LocalDateTime.now());
        message3.setStatus(Status.SENT);
        message3.setMessageText("Как дела?");
        message3.setAuthorId(getPeople().get(0));
        message3.setRecipientId(getPeople().get(1));
        message3.setDialogId(getDialogs().get(0));

        return List.of(message1, message2, message3);
    }

    private List<Person> getPeople() {
        Person person1 = new Person();
        Person person2 = new Person();

        person1.setId(1L);
        person1.setFirstName("Alla");
        person1.setLastName("Pugacheva");

        person2.setId(2L);
        person2.setFirstName("Crazy");
        person2.setLastName("Frog");

        return List.of(person1, person2);
    }

    private List<Dialog> getDialogs() {
        Dialog dialog = new Dialog();

        dialog.setId(1L);
        dialog.setCompanion1(getPeople().get(0));
        dialog.setCompanion2(getPeople().get(1));

        Message message = new Message();

        message.setId(3L);
        message.setTime(LocalDateTime.now());
        message.setStatus(Status.SENT);
        message.setMessageText("Как дела?");
        message.setAuthorId(getPeople().get(0));
        message.setRecipientId(getPeople().get(1));
        message.setDialogId(dialog);

        dialog.setMessages(List.of(message));

        return List.of(dialog);
    }
}
