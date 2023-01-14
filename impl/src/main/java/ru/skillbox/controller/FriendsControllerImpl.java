package ru.skillbox.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.common.SearchPersonDto;
import ru.skillbox.enums.StatusCode;
import ru.skillbox.exception.UserNotFoundException;
import ru.skillbox.model.FriendsController;
import ru.skillbox.model.User;
import ru.skillbox.response.ErrorResponse;
import ru.skillbox.response.data.PersonDto;
import ru.skillbox.service.FriendsService;
import ru.skillbox.service.SearchService;
import ru.skillbox.service.UserService;

@AllArgsConstructor
@RestController
@Slf4j
public class FriendsControllerImpl implements FriendsController {
    private final FriendsService friendsService;
    private final UserService userService;
    private final SearchService searchService;

    @Override
    public ResponseEntity<Page<PersonDto>> getRelationships(SearchPersonDto dto) {
        StatusCode statusCode = dto.getStatusCode();
        Long current = userService.getCurrentUser().getId();
        log.debug("user = {} looking for = {}", current, statusCode);
        return ResponseEntity
                .ok(new PageImpl<>(
                        friendsService
                                .getRelationsForByCode(
                                        current,
                                        statusCode)));
    }

    @Override
    public ResponseEntity<String> sendFriendRequest(Long id) {
        Long currentUser = userService.getCurrentUser().getId();
        log.debug("user = {} sending request to = {}", currentUser, id);
        try {
            return ResponseEntity
                    .ok(friendsService
                            .sendFriendRequest(
                                    currentUser,
                                    id));
        } catch (UserNotFoundException e) {
            log.error("sendFriendRequest throws {}", e.getMessage());

            //FixMe to smthg better it is not ok! need front rework maybe
            return ResponseEntity
                    .ok("Ok");
        }
    }

    @Override
    public ResponseEntity<String> approveFriendRequest(Long id) {
        Long currentUser = userService.getCurrentUser().getId();
        log.info("user = {} approving request to = {}", currentUser, id);
        try {
            return ResponseEntity
                    .ok(friendsService
                            .approveFriends(
                                    currentUser,
                                    id));
        } catch (UserNotFoundException e) {
            log.info("approveFriendRequest throws {}", e.getMessage());

            //FixMe to smthg better it is not ok! need front rework maybe
            return ResponseEntity
                    .ok("Ok");
        }
    }

    @Override
    public ResponseEntity<String> deleteFriend(Long id) {
        User currentUser = userService.getCurrentUser();
        log.info("user = {} delete friend = {}", currentUser.getId(), id);

        return ResponseEntity.ok(friendsService.deleteFriend(currentUser.getId(), id));
    }

    @Override
    public ResponseEntity<String> subscribe(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        try {
            return ResponseEntity.ok(friendsService.subscribe(currentUser.getId(), id));
        } catch (UserNotFoundException e) {
            log.info("subscribe {}", e.getMessage());
            return ResponseEntity.ok("Ok");
        }
    }

    @Override
    public ResponseEntity<String> block(Long id) {
        User currentUser = userService.getCurrentUser();
        try {
            return ResponseEntity.ok(friendsService.blockFriend(currentUser.getId(), id));
        } catch (UserNotFoundException e) {
            log.info("block {}", e.getMessage());
            return ResponseEntity.ok("Ok");
        }
    }

    @Override
    public ResponseEntity<Object> recommendations() {
        try {
            return ResponseEntity.ok(searchService.searchRecommendations());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse().getResponse(e.getMessage()));
        }
    }

    @Override
    public int count() {
        return friendsService
                .getFriendsRequestsCountFor(userService.getCurrentUser().getId());
    }
}
