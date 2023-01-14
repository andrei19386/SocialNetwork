package ru.skillbox.response.settings;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.common.AccountDto;
import ru.skillbox.enums.NotificationType;

@Getter
@Setter
public class NotificationDataRs {

    private Long id;
    @JsonProperty(value = "author")
    private AccountDto author;
    private String content;
    @JsonProperty(value = "notificationType")
    private NotificationType notificationType;
    private String sentTime;

    public static NotificationDataRs setNotificationDataRs(Long id,
                                                           AccountDto accountDto,
                                                           String content,
                                                           NotificationType notificationType,
                                                           String sentTime) {
        NotificationDataRs notificationDataRs = new NotificationDataRs();
        notificationDataRs.setId(id);
        notificationDataRs.setAuthor(accountDto);
        notificationDataRs.setContent(content);
        notificationDataRs.setNotificationType(notificationType);
        notificationDataRs.setSentTime(sentTime);
        return notificationDataRs;
    }
}
