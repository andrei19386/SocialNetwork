package ru.skillbox.response.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.common.AccountDto;

@Getter
@Setter
@Builder
public class DialogDto {

    private Long id;
    private AccountDto conversationPartner;
    private Long unreadCount;
    private MessageDto lastMessage;
}
