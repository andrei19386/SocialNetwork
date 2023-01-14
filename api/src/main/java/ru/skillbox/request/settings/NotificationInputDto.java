package ru.skillbox.request.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.NotificationType;

@Getter
@Setter
public class NotificationInputDto {

    private Long authorId;
    private Long userId;
    @JsonProperty(value = "notificationType")
    private NotificationType notificationType;
    private String content;

}