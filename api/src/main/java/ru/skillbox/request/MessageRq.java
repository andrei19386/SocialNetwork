package ru.skillbox.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.response.data.MessageDto;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRq {

    private String type;
    private Long accountId;
    private MessageDto data;
}
