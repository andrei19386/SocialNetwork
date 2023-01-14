package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillbox.common.AccountDto;
import ru.skillbox.model.Dialog;
import ru.skillbox.model.Message;
import ru.skillbox.model.Person;
import ru.skillbox.response.data.DialogDto;
import ru.skillbox.response.data.MessageDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DialogMapper {

    @Mapping(target = "conversationPartner", qualifiedByName = "account")
    DialogDto DialogToDto(Dialog dialog);

    @Mapping(target = "authorId", qualifiedByName = "personToAccount")
    @Mapping(target = "recipientId", qualifiedByName = "personToAccount")
    @Mapping(target = "time", qualifiedByName = "timeToLong")
    MessageDto MessageToDto(Message message);

    @Mapping(target = "authorId", qualifiedByName = "idToPerson")
    @Mapping(target = "recipientId", qualifiedByName = "idToPerson")
    @Mapping(target = "time", qualifiedByName = "longToTime")
    Message DtoToMessage(MessageDto messageDto);

    @Named("personToAccount")
    default Long personToAccount(Person person) {
        return person == null ? null : person.getId();
    }

    @Named("idToPerson")
    default Person idToPerson(Long id) {
        Person person = new Person();
        person.setId(id);
        return person;
    }

    List<DialogDto> listDialogToDto(List<Dialog> dialogs);


    List<MessageDto> listMessageToDto(List<Message> messages);

    @Named("account")
    default AccountDto account(Person person) {
        return AccountMapper.INSTANCE.personToAccountDto(person);
    }

    @Named("timeToLong")
    default Long timeToString(LocalDateTime time) {
        return time == null ? System.currentTimeMillis() / 1000
                : Timestamp.valueOf(time).getTime() / 1000;
    }

    @Named("longToTime")
    default LocalDateTime stringToTime(Long time) {
        return Instant.ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
