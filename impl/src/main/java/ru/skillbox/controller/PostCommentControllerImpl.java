package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.model.PostCommentController;
import ru.skillbox.request.CommentAddRequest;
import ru.skillbox.service.PostCommentService;

@RestController
@RequiredArgsConstructor
public class PostCommentControllerImpl implements PostCommentController {

    private final PostCommentService postCommentService;
    @Value("{${isTest}}")
    private String isTestString;


    @Override
    public void addCommentByIdPost(String id, CommentAddRequest request) {
        postCommentService.addComment(id, request, isTestString.equals("{true}"));
    }

    @Override
    public void deleteCommentByIdPost(String id, String commentId) {
        postCommentService.deleteComment(id, commentId);
    }

    @Override
    public void putCommentByIdPost(CommentAddRequest request, String id, String commentId) {
        postCommentService.updateComment(request, id, commentId);
    }
}
