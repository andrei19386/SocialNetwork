package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.enums.Role;
import ru.skillbox.exception.CaptchaException;
import ru.skillbox.exception.EmailNotFoundException;
import ru.skillbox.exception.InvalidCredentialsException;
import ru.skillbox.exception.UserIsAlreadyRegisteredException;
import ru.skillbox.jwt.JwtTokenProvider;
import ru.skillbox.model.User;
import ru.skillbox.request.LoginRequest;
import ru.skillbox.request.PasswordRecoveryRequest;
import ru.skillbox.request.RegistrationRequest;
import ru.skillbox.response.LoginResponse;
import ru.skillbox.response.PasswordRecoveryResponse;
import ru.skillbox.response.Responsable;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CaptchaFileService captchaFileService;

    public Responsable login(LoginRequest request) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(request.getEmail());
        if (!userService.passwordCheck(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        if (!user.isEnabled()) {
            throw new InvalidCredentialsException();
        }
        if (!user.isAccountNonLocked()) {
            throw new InvalidCredentialsException();
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_USER.getValue());
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of(authority));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication.getName(), "ROLE_USER");
        return new LoginResponse().getResponse(jwt);
    }

    public Responsable passwordRecovery(PasswordRecoveryRequest request) throws EmailNotFoundException {
        String email = request.getEmail();
        User user = userService.getUserByEmail(email);
        String password = generateRandomPassword(8);
        userService.setNewPassword(user, password);
        emailService.passwordRecoveryMessage(email, password);
        return new PasswordRecoveryResponse().getResponse("ok");
    }

    public void registration(RegistrationRequest request) throws UserIsAlreadyRegisteredException {
        if(captchaFileService.getCaptchaFileByName(request.getCode()).isEmpty()){
            throw new CaptchaException();
        }
        userService.registration(request);
    }

    public void logout() {
        GrantedAuthority authority = new SimpleGrantedAuthority(Role.ROLE_ANONYMOUS.getValue());
        Authentication authenticationToken = new AnonymousAuthenticationToken("anonymousUser", "anonymousUser", List.of(authority));
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    private String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }
}
