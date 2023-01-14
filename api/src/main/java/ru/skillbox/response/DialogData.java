package ru.skillbox.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
//todo удалить если не используется

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialogData {

    private Long id;

    private Long time;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("message_text")
    private String messageText;

    private String message;

    private Long count;

}

