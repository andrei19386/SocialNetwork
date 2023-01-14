package ru.skillbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.exception.NotAuthorizedException;
import ru.skillbox.exception.UserIsAlreadyRegisteredException;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.model.User;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.request.RegistrationRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PersonService personService;




    public void registration(RegistrationRequest request) throws UserIsAlreadyRegisteredException {
        Optional<User> userFromDB = userRepository.findByEmail(request.getEmail());
        if (userFromDB.isPresent()) {
            throw new UserIsAlreadyRegisteredException();
        }
        registrationInDB(request);
        User user = getUserByEmail(request.getEmail());
        personService.registrationPerson(user, request);
    }

    private void registrationInDB(RegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword1()));
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public boolean passwordCheck(String newPassword, String currentPassword) {
        return bCryptPasswordEncoder.matches(newPassword, currentPassword);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    public User getUserByEmail(String email) {
        return loadUserByUsername(email);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    public void setNewPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public User getCurrentUser() throws NotAuthorizedException {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loadUserByUsername(email);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteCurrentUser() {
        User user = getCurrentUser();
        user.setEnabled(false);
        personService.deletePerson(personService.getCurrentPerson());
        save(user);
    }

    public void isBlockUserById(Long id, boolean isBlock) {
        User user = getUserById(id);
        user.setAccountNonLocked(!isBlock);
        personService.isBlock(id, isBlock);
        save(user);
    }
}
