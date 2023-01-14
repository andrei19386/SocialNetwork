package ru.skillbox.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse implements Responsable {
    private String accessToken;
    private String refreshToken;

    @Override
    public Responsable getResponse(String s) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(s);
        loginResponse.setRefreshToken(s);
        return loginResponse;
    }
}
