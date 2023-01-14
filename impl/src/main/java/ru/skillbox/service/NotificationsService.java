package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.common.SearchPersonDto;
import ru.skillbox.enums.NotificationType;
import ru.skillbox.enums.Status;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.mapper.AccountMapper;
import ru.skillbox.model.*;
import ru.skillbox.repository.MessageRepository;
import ru.skillbox.repository.NotificationRepository;
import ru.skillbox.request.settings.NotificationInputDto;
import ru.skillbox.response.data.PersonDto;
import ru.skillbox.response.settings.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationRepository notificationRepository;
    private final PersonService personService;
    private final EmailService emailService;
    private final SettingService settingService;

    public NotificationSentDto getNotifications() {
        NotificationSentDto notificationSentDto = new NotificationSentDto();
        List<NotificationDataRs> dataRs = saveAndGetAllNotificationInNotificationDataRs();
        notificationSentDto.setData(dataRs);
        notificationSentDto.setTimeStamp(new Date().toString());
        return notificationSentDto;
    }


    public NotificationCountRs getNotificationCount() {
        int count = saveAndGetAllNotificationInNotificationDataRs().size();
        NotificationCountRs notificationCountRs = new NotificationCountRs();
        CountRs countRs = new CountRs();
        countRs.setCount(count);
        notificationCountRs.setCountRs(countRs);
        notificationCountRs.setTimestamp(new Date().getTime());
        return notificationCountRs;
    }


    public NotificationDto postNotificationDto(NotificationInputDto notificationInputDto) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notificationInputDto.getUserId());
        notificationDto.setNotificationType(notificationInputDto.getNotificationType());
        notificationDto.setAuthorId(notificationInputDto.getAuthorId());
        notificationDto.setContent(notificationInputDto.getContent());
        notificationDto.setTime(new Date().toString());
        notificationDto.setUserId(notificationInputDto.getUserId());
        notificationDto.setStatusSent(false);
        return notificationDto;
    }


    public List<NotificationDataRs> saveAndGetAllNotificationInNotificationDataRs() throws UserNotFoundException {
        List<NotificationDataRs> dataRs = new ArrayList<>();
        for (Notification notification1 : getListNotification()) {
            if (!notification1.isRead()) {
                Person id = personService.getPersonById(notification1.getPersonId());
                NotificationDataRs notificationDataRs = new NotificationDataRs();
                notificationDataRs.setId(notification1.getId());
                notificationDataRs.setNotificationType(notification1.getNotificationType());
                notificationDataRs.setSentTime(new Date().toString());
                notificationDataRs.setContent(notification1.getContent());
                notificationDataRs.setAuthor(AccountMapper
                        .INSTANCE
                        .personToAccountDto(id));
                dataRs.add(notificationDataRs);
            }
        }
        return dataRs;
    }


    public List<Notification> getListNotification() {
        Person person = personService.getCurrentPerson();
        return notificationRepository
                .findByPersonId(person.getId());
    }

    public void createAndSaveNotification(NotificationInputDto inputDto){
        Settings settings = settingService.getSettingNotifById(inputDto.getUserId());
        if ((settings.isFriendRequest() &&
                inputDto.getNotificationType().equals(NotificationType.FRIEND_REQUEST)) ||
                (settings.isPost() &&
                        inputDto.getNotificationType().equals(NotificationType.POST)) ||
                (settings.isPostComment() &&
                        inputDto.getNotificationType().equals(NotificationType.POST_COMMENT)) ||
                (settings.isMessage() &&
                        inputDto.getNotificationType().equals(NotificationType.MESSAGE)) ||
                (settings.isSendEmailMessage() &&
                        inputDto.getNotificationType().equals(NotificationType.SEND_EMAIL_MESSAGE)) ||
                (settings.isCommentComment() &&
                        inputDto.getNotificationType().equals(NotificationType.COMMENT_COMMENT)) ||
                (settings.isFriendBirthday() &&
                        inputDto.getNotificationType().equals(NotificationType.FRIEND_BIRTHDAY))){

            Notification notification = new Notification();
            notification.setPersonId(inputDto.getUserId());
            notification.setNotificationType(inputDto.getNotificationType());
            notification.setAuthorId(inputDto.getAuthorId());
            notification.setContent(inputDto.getContent());
            notificationRepository.save(notification);
            if (settings.isSendEmailMessage()) {
                emailService.sendSimpleMessage(
                        personService.getPersonById(notification.getPersonId()).getEmail(),
                        notification.getNotificationType().getName(),
                        notification.getContent());
            }
        }


    }
}
