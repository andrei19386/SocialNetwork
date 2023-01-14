package ru.skillbox.model;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.request.LoginRequest;
import ru.skillbox.request.PasswordRecoveryRequest;
import ru.skillbox.request.RegistrationRequest;
import ru.skillbox.response.CaptchaResponse;
import ru.skillbox.response.LoginResponse;
import ru.skillbox.response.PasswordRecoveryResponse;
import ru.skillbox.response.Responsable;

@RequestMapping("/api/v1/auth")
public interface AuthController {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются поля timestamp, data, accessToken и tokenType)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос (возвращаются поля error и error_description)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    }
            )
    })
    @PostMapping("/login")
    ResponseEntity<Responsable> login(@RequestBody LoginRequest request);

    @PostMapping("/logout")
    void logout();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются поля timestamp и data)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Responsable.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос (возвращаются поля error и error_description)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Responsable.class))
                    }
            )
    })
    @PostMapping("/register")
    ResponseEntity<Responsable> registration(@RequestBody RegistrationRequest request);

    @GetMapping("/captcha")
    ResponseEntity<CaptchaResponse> captcha();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешный вход (возвращаются поля timestamp и data)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PasswordRecoveryResponse.class))
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос (возвращаются поля error и error_description)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PasswordRecoveryResponse.class))
                    }
            )
    })
    @PostMapping("/password/recovery")
    ResponseEntity<Responsable> passwordRecovery(@RequestBody PasswordRecoveryRequest request);
}
