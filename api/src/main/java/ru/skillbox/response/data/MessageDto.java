package ru.skillbox.response.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.Status;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {

    private Long id;
    private Long time;
    private Status status;
    private String messageText;
    private Long authorId;
    private Long recipientId;
}
