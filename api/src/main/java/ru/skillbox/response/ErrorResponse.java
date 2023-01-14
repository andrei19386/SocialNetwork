package ru.skillbox.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse implements Responsable {

    @JsonProperty("error_description")
    private String errorDescription;
    private String error;
    private Long timestamp;

    @Override
    public Responsable getResponse(String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Неверный запрос");
        errorResponse.setErrorDescription(message);
        errorResponse.setTimestamp(new Date().getTime());
        return errorResponse;
    }
}
