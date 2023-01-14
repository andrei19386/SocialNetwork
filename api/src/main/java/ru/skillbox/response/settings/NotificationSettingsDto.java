package ru.skillbox.response.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.request.settings.SettingRq;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NotificationSettingsDto {

    private String time;

    @JsonProperty(value = "data")
    private List<SettingRq> listSettings = new ArrayList<>();

    @JsonProperty(value = "user_id")
    private Long userId;
}
