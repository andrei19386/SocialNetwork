package ru.skillbox.request.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.enums.NotificationType;

@Getter
@Setter
public class SettingRq {

    @JsonProperty(value = "notification_type")
    private NotificationType notificationType;
    private boolean enable;

    public static SettingRq getSettingRq(NotificationType name, boolean enable) {
        SettingRq settingRq = new SettingRq();
        settingRq.setNotificationType(name);
        settingRq.setEnable(enable);
        return settingRq;
    }
}