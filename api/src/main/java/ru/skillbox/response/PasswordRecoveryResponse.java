package ru.skillbox.response;

import lombok.Data;
import ru.skillbox.common.AbstractDto;

@Data
public class PasswordRecoveryResponse implements Responsable{
    private AbstractDto data;

    @Override
    public Responsable getResponse(String s) {
        PasswordRecoveryResponse passwordRecoveryResponse = new PasswordRecoveryResponse();
        passwordRecoveryResponse.setData(new AbstractDto(s));
        return passwordRecoveryResponse;
    }
}
