package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.LikeController;
import ru.skillbox.service.LikeService;

@RestController
@RequiredArgsConstructor
public class LikeControllerImpl implements LikeController {

    @Value("{${isTest}}")
    private String isTestString;
    private final LikeService likeService;

    @Override
    public void createPostLike(String id) {
        likeService.addPostLike(id, isTestString.equals("true"));
    }

    @Override
    public void likeToComment(String id, String commentId) {
        likeService.addCommentLike(id, commentId, isTestString.equals("{true}"));
    }

    @Override
    public void deleteLike(String id) {
        likeService.deletePostLike(id, isTestString.equals("true"));
    }

    @Override
    public void deleteLikeToComment(String id, String commentId) {
        likeService.deleteCommentLike(id, commentId, isTestString.equals("{true}"));
    }
}
