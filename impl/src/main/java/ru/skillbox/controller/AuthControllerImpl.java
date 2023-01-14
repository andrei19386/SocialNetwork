package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.AuthController;
import ru.skillbox.request.LoginRequest;
import ru.skillbox.request.PasswordRecoveryRequest;
import ru.skillbox.request.RegistrationRequest;
import ru.skillbox.response.CaptchaResponse;
import ru.skillbox.response.ErrorResponse;
import ru.skillbox.response.Responsable;
import ru.skillbox.service.AuthService;
import ru.skillbox.service.CaptchaFileService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;
    private final CaptchaFileService captchaFileService;


    @Override
    public ResponseEntity<Responsable> login(LoginRequest request) {
        try {
            log.info("{} is login in", request.getEmail());
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            log.error("login attempt for {}", request.getEmail());
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Responsable> registration(RegistrationRequest request) {
        try {
            authService.registration(request);
            log.info("{} registered", request.getEmail());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<Responsable> passwordRecovery(PasswordRecoveryRequest request) {
        try {
            return ResponseEntity.ok(authService.passwordRecovery(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    public void logout() {
        authService.logout();
    }

    @Override
    public ResponseEntity<CaptchaResponse> captcha() {
        return ResponseEntity.ok(captchaFileService.generateCaptchaResponse());
    }


}
