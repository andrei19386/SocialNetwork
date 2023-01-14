package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.enums.Status;
import ru.skillbox.model.Dialog;
import ru.skillbox.model.Message;
import ru.skillbox.model.Person;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByDialogId(Dialog dialog);

    List<Message> findAllByRecipientIdAndStatus(Person recipient, Status status);

}
