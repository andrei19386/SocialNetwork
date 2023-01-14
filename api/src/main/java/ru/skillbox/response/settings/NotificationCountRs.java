package ru.skillbox.response.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationCountRs {

    private long timestamp;
    @JsonProperty(value = "data")
    private CountRs countRs;
}
