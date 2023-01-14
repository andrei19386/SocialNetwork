package ru.skillbox.response.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationSentDto {

    private String timeStamp;
    @JsonProperty(value = "data")
    private List<NotificationDataRs> data;
}
