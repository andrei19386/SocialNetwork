package ru.skillbox.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.enums.NotificationType;
import ru.skillbox.model.Person;
import ru.skillbox.model.Settings;
import ru.skillbox.repository.SettingRepository;
import ru.skillbox.request.settings.SettingRq;
import ru.skillbox.response.settings.NotificationSettingsDto;
import ru.skillbox.response.settings.SettingsDto;

import java.util.Date;

@Service
public class SettingService {

    private final PersonService personService;

    private final SettingRepository settingRepository;

    @Autowired
    public SettingService(PersonService personService, SettingRepository settingRepository){
        this.personService = personService;
        this.settingRepository = settingRepository;
    }

    public NotificationSettingsDto makeSetting() {
        Person person = personService.getCurrentPerson();
        Settings settings = getSettingNotifById(person.getId());
        NotificationSettingsDto settingsDto = new NotificationSettingsDto();
        settingsDto.setTime(new Date().toString());
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.FRIEND_REQUEST, settings.isFriendRequest()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.FRIEND_BIRTHDAY, settings.isFriendBirthday()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.POST_COMMENT, settings.isPostComment()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.COMMENT_COMMENT, settings.isCommentComment()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.POST, settings.isPost()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.MESSAGE, settings.isMessage()));
        settingsDto.getListSettings().add(SettingRq.getSettingRq(
                NotificationType.SEND_EMAIL_MESSAGE, settings.isSendEmailMessage()));
        settingsDto.setUserId(settings.getId());
        return settingsDto;
    }


    public void saveSettings(NotificationType notification, boolean bool) {
        Person person = personService.getCurrentPerson();
        Settings setting = getSettingNotifById(person.getId());
        switch (notification) {
            case POST: {
                setting.setPost(bool);
                break;
            }
            case POST_COMMENT: {
                setting.setPostComment(bool);
                break;
            }
            case MESSAGE: {
                setting.setMessage(bool);
                break;
            }
            case SEND_EMAIL_MESSAGE: {
                setting.setSendEmailMessage(bool);
                break;
            }
            case COMMENT_COMMENT: {
                setting.setCommentComment(bool);
                break;
            }
            case FRIEND_BIRTHDAY: {
                setting.setFriendBirthday(bool);
                break;
            }
            case FRIEND_REQUEST: {
                setting.setFriendRequest(bool);
                break;
            }
        }
        settingRepository.save(setting);
    }


    public SettingsDto compareSettings(SettingsDto settingsDto) {
        Person person = personService.getCurrentPerson();
        Settings settingNotification = getSettingNotifById(person.getId());
        if (settingNotification.getId().equals(settingsDto.getId())) {
            settingNotification.setPost(settingsDto.isPost());
            settingNotification.setMessage(settingsDto.isMessage());
            settingNotification.setCommentComment(settingsDto.isCommentComment());
            settingNotification.setPostComment(settingsDto.isPostComment());
            settingNotification.setFriendBirthday(settingsDto.isFriendBirthday());
            settingNotification.setFriendRequest(settingsDto.isFriendRequest());
            settingNotification.setSendEmailMessage(settingsDto.isSendEmailMessage());
            settingNotification.setSendPhoneMessage(settingsDto.isSendPhoneMessage());
            settingRepository.save(settingNotification);
        }
        return settingsDto;
    }

    public Settings getSettingNotifById(Long id) {
        return settingRepository.findById(id).get();
    }
}
