package ru.skillbox.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractResponse {
    @JsonIgnore
    private boolean isSuccess;

    @JsonProperty("error_description")
    private String errorDescription;

    private String error;

    private Long timestamp;

}
