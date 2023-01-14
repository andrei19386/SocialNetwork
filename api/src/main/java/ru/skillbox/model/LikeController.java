package ru.skillbox.model;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/post/{id}")
public interface LikeController {

    @PostMapping("/like")
    void createPostLike(@PathVariable String id);

    @PostMapping("/comment/{commentId}/like")
    void likeToComment(@PathVariable String id, @PathVariable String commentId);

    @DeleteMapping("/like")
    void deleteLike(@PathVariable String id);

    @DeleteMapping("/comment/{commentId}/like")
    void deleteLikeToComment(@PathVariable String id, @PathVariable String commentId);
}
